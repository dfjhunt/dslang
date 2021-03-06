
package dslang.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

import dslang.monad.wrapper.OptionM;

public class StreamUtilTest {

    class Node {
        Node _left;

        Node _right;

        String _id;

        public Node(String id, Node left, Node right) {
            _left = left;
            _right = right;
            _id = id;
        }

        public Node(String id) {
            _id = id;
        }
    }

    @Test
    public void testRecurseBranching() {
        Node ll = new Node("ll");
        Node lr = new Node("lr");
        Node rl = new Node("rl");
        Node rr = new Node("rr");
        Node l = new Node("l", ll, lr);
        Node r = new Node("r", rl, rr);
        Node root = new Node("root", l, r);

        Stream<Node> str = StreamUtil.recurse(root, x -> Stream.of(x._left, x._right));

        Assert.assertEquals(Arrays.asList("root", "l", "ll", "lr", "r", "rl", "rr"), str.map(x -> x._id).collect(Collectors.toList()));
    }

    @Test
    public void testRecurse() {
        Assert.assertEquals(Arrays.asList(0, 1, 2, 3, 4, 5), StreamUtil.recurse(i -> i < 5, i -> i + 1, 0).collect(Collectors.toList()));
    }

    @Test
    public void testUnfold() {
        Stream<Integer> s =
            StreamUtil.unfold(x -> true, p -> Pair.of(p.left() + p.right(), Pair.of(p.right(), p.left() + p.right())), Pair.of(0, 1));
        Assert.assertEquals(Arrays.asList(1, 2, 3, 5, 8, 13, 21, 34, 55, 89), s.limit(10).collect(Collectors.toList()));
    }

    @Test
    public void testUnfold_takeWhile() {
        Stream<Integer> str = StreamUtil.unfold(i -> {
            if (i.hasNext()) {
                Integer temp = i.next();
                if (temp < 5) {
                    return OptionM.of(Pair.of(temp, i));
                }
            }
            return OptionM.empty();
        }, (Iterator<Integer>) Stream.iterate(0, i -> i + 1).iterator());

        Assert.assertEquals(Arrays.asList(0, 1, 2, 3, 4), str.collect(Collectors.toList()));
    }

    @Test
    public void testScan() {
        Stream<Integer> str = StreamUtil.scan((x, y) -> x + y, 0, Stream.iterate(0, i -> i + 1));
        Assert.assertEquals(Arrays.asList(0, 1, 3, 6, 10, 15), str.limit(6).collect(Collectors.toList()));
    }

    @Test
    public void testTakeWhile() {
        Stream<Integer> str = StreamUtil.takeWhile(i -> i < 10, Stream.iterate(0, i -> i + 1));
        Assert.assertEquals(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), str.collect(Collectors.toList()));
    }
}
