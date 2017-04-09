package dslang.monad;

import org.junit.Assert;
import org.junit.Test;

import dslang.monad.wrapper.OptionM;
import dslang.util.Pair;

public class ForTest {

    @Test
    public void testForBiFunction() {
        OptionM<Integer> opt = For.of(OptionM.sunit(3), OptionM.sunit(5), (x, y) -> x + y);

        Assert.assertTrue(opt.isPresent());
        Assert.assertEquals((int) opt.get(), (int) 8);
    }

    @Test
    public void testForBiFunction2() {
        OptionM<Integer> emp = OptionM.empty();
        OptionM<Integer> opt = For.of(emp, OptionM.sunit(5), (x, y) -> x + y);

        Assert.assertFalse(opt.isPresent());
    }

    @Test
    public void testForBiFunction3() {
        OptionM<Integer> emp = OptionM.empty();
        OptionM<Integer> opt = For.of(OptionM.sunit(5), emp, (x, y) -> x + y);

        Assert.assertFalse(opt.isPresent());
    }

    @Test
    public void testForTriFunction() {
        OptionM<Integer> opt = For.of(OptionM.sunit(3), OptionM.sunit(5), OptionM.sunit(2), (x, y, z) -> x + y + z);

        Assert.assertTrue(opt.isPresent());
        Assert.assertEquals((int) opt.get(), (int) 10);
    }

    @Test
    public void testForTriFunction2() {
        OptionM<Integer> emp = OptionM.empty();
        OptionM<Integer> opt = For.of(emp, OptionM.sunit(5), OptionM.sunit(2), (x, y, z) -> x + y + z);

        Assert.assertFalse(opt.isPresent());
    }

    @Test
    public void testFor2MonadsWValuePassing() {
        State<Integer, Void> t = //
                        For.of(State.<Integer> get(), //
                               s -> State.set(s + 1), //
                               (x, y) -> null);

        Assert.assertEquals(Pair.of(null, 3), t.run(2));
    }
}
