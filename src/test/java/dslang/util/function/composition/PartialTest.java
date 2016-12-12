
package dslang.util.function.composition;

import static dslang.util.function.composition.Hole.__;
import static org.junit.Assert.assertEquals;

import java.util.function.Function;

import org.junit.Test;

import dslang.util.function.composition.Partial;

public class PartialTest {

    public static int minus(int a, int b) {
        return a - b;
    }

    @Test
    public void testA__() {
        Function<Integer, Integer> fiveMinus = Partial.of(PartialTest::minus,5, __);
        assertEquals((int)fiveMinus.apply(3), (int) 2);
    }
    
    @Test
    public void test__B() {
        Function<Integer, Integer> minusTwo = Partial.of(PartialTest::minus, __, 2);
        assertEquals((int)minusTwo.apply(5), (int) 3);
    }

}
