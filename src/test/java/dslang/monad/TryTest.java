
package dslang.monad;

import java.util.function.Function;

import org.junit.Assert;
import org.junit.Test;

import dslang.util.function.CheckedFunction;

public class TryTest {

    @Test
    public void testCheck() {
        CheckedFunction<Integer, Integer, Exception> f = x -> {
            throw new Exception("Test");
        };

        Function<Integer, Try<Integer>> fChecked = Try.check(f);

        Try<Integer> t = fChecked.apply(3);
        
        Assert.assertTrue(t.isException());
    }

}
