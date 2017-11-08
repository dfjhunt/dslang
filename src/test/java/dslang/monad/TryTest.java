
package dslang.monad;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.junit.Assert;
import org.junit.Test;

import dslang.util.function.TriFunction;
import dslang.util.function.checked.CheckedBiFunction;
import dslang.util.function.checked.CheckedFunction;
import dslang.util.function.checked.CheckedTriFunction;

public class TryTest {

    @Test
    public void testCheckFunctionWException() {
        CheckedFunction<Integer, Integer, Exception> f = x -> {
            throw new Exception("Test");
        };

        Function<Integer, Try<Integer>> fChecked = Try.check(f);

        Try<Integer> t = fChecked.apply(3);

        Assert.assertTrue(t.isException());
    }

    @Test
    public void testCheckFunction() throws Exception{
        CheckedFunction<Integer, Integer, Exception> f = x -> {
            return x;
        };

        Function<Integer, Try<Integer>> fChecked = Try.check(f);

        Try<Integer> t = fChecked.apply(3);

        Assert.assertFalse(t.isException());
        Assert.assertEquals((long) 3, (long) t.get());
    }
    
    @Test
    public void testCheckBiFunctionWException() {
        CheckedBiFunction<Integer, Integer, Integer, Exception> f = (a,b) -> {
            throw new Exception("Test");
        };

        BiFunction<Integer, Integer, Try<Integer>> fChecked = Try.check(f);

        Try<Integer> t = fChecked.apply(2, 5);

        Assert.assertTrue(t.isException());
    }

    @Test
    public void testCheckBiFunction() throws Exception{
        CheckedBiFunction<Integer, Integer, Integer, Exception> f = (a,b) -> a+b;

        BiFunction<Integer, Integer, Try<Integer>> fChecked = Try.check(f);

        Try<Integer> t = fChecked.apply(2, 5);

        Assert.assertFalse(t.isException());
        Assert.assertEquals((long) 7, (long) t.get());
    }
    
    @Test
    public void testCheckTriFunctionWException() {
        CheckedTriFunction<Integer, Integer, Integer, Integer, Exception> f = (a,b,c) -> {
            throw new Exception("Test");
        };

        TriFunction<Integer, Integer, Integer, Try<Integer>> fChecked = Try.check(f);

        Try<Integer> t = fChecked.apply(1, 2, 3);

        Assert.assertTrue(t.isException());
    }

    @Test
    public void testCheckTriFunction() throws Exception{
        CheckedTriFunction<Integer, Integer, Integer, Integer, Exception> f = (a,b,c) -> a+b+c;

        TriFunction<Integer, Integer, Integer, Try<Integer>> fChecked = Try.check(f);

        Try<Integer> t = fChecked.apply(1, 2, 3);

        Assert.assertFalse(t.isException());
        Assert.assertEquals((long) 6, (long) t.get());
    }

}
