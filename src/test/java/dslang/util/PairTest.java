package dslang.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

public class PairTest {
    
    @Test
    public void testToString(){
        Pair<Integer, Integer> a = Pair.of(1,  2);
        
        Assert.assertEquals("Pair(1, 2)", a.toString());
    }
    
    @Test
    public void testEquals(){
        Pair<Integer, Integer> a = Pair.of(1, 2);
        Pair<Integer, Integer> b = Pair.of(1, 2);
        Pair<String, String> c = Pair.of("test", "test");
        Pair<Integer, Integer> d = Pair.of(1, 3);
        
        Assert.assertEquals(a,  b);
        Assert.assertNotEquals(a, c);
        Assert.assertNotEquals(d, a);
        Assert.assertNotEquals(null, a);
    }
    
    @Test
    public void testZip(){
        //putting the probe in the stream will allow me to determine when it is iterated so I can prove zip is lazy
        Probe<Boolean> p1 = new Probe<Boolean>(false);
        Stream<Integer> s1 = Stream.iterate(1, i -> {p1.set(true); return i + 1;});
        Stream<String> s2 = Stream.of("a", "b", "c");
        
        Stream<Pair<Integer, String>> zipped = Pair.zip(s1,  s2);
        List<Pair<Integer, String>> expected = Arrays.asList(Pair.of(1, "a"), Pair.of(2, "b"), Pair.of(3, "c"));
        
        //if zip is lazy the integer stream should not have been iterated
        Assert.assertFalse(p1.get());
        
        Assert.assertEquals(expected, zipped.collect(Collectors.toList()));
        
        //now that the zipped stream has been iterated the original streams will have also been
        Assert.assertTrue(p1.get());
    }
}
