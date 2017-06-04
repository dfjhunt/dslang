
package dslang.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import dslang.monad.Either;
import dslang.monad.wrapper.ListM;
import dslang.monad.wrapper.OptionM;

public class UtilityTest {

    @Test
    public void testListSequence() {
        List<OptionM<Integer>> lo = new ArrayList<>();
        lo.add(OptionM.sunit(1));
        lo.add(OptionM.sunit(2));
        lo.add(OptionM.sunit(3));

        OptionM<List<Integer>> omli = Utility.sequence(OptionM::sunit, lo);

        Assert.assertEquals(omli.get(), Arrays.asList(1, 2, 3));
    }

    @Test
    public void testListMSequence() {
        ListM<OptionM<Integer>> lo = new ListM<OptionM<Integer>>();
        lo.add(OptionM.sunit(1));
        lo.add(OptionM.sunit(2));
        lo.add(OptionM.sunit(3));

        OptionM<ListM<Integer>> omli = Utility.sequenceListM(OptionM::sunit, lo);

        Assert.assertEquals(omli.get().unwrap(), Arrays.asList(1, 2, 3));
    }

    @Test
    public void testListMSequenceShortCircuit() {
        ListM<OptionM<Integer>> lo = new ListM<OptionM<Integer>>();
        lo.add(OptionM.empty());
        lo.add(OptionM.sunit(2));
        lo.add(OptionM.sunit(3));

        OptionM<ListM<Integer>> omli = Utility.sequenceListM(OptionM::sunit, lo);

        Assert.assertFalse(omli.isPresent());
    }

    @Test
    public void testListTraverse() {
        List<Integer> li = new ArrayList<>();
        li.add(1);
        li.add(null);
        li.add(3);

        OptionM<List<Either<Void, Integer>>> omli = Utility.traverse(OptionM::sunit, x->(OptionM<Either<Void,Integer>>)OptionM.sunit(x).toEither(), li);

        Assert.assertTrue(omli.isPresent());
        Assert.assertEquals(Either.right(1), omli.get().get(0));
        Assert.assertEquals(Either.left(null), omli.get().get(1));
        Assert.assertEquals(Either.right(3), omli.get().get(2));
        Assert.assertNotEquals(Either.right(3), omli.get().get(0));
    }

    @Test
    public void testFlatten() {
        ListM<ListM<Integer>> lo = new ListM<ListM<Integer>>();
        lo.add(new ListM<Integer>(Arrays.asList(1, 2, 3)));
        lo.add(new ListM<Integer>(Arrays.asList(4, 5, 6)));

        ListM<Integer> li = Utility.flatten(lo);

        Assert.assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6), li.unwrap());
    }

}
