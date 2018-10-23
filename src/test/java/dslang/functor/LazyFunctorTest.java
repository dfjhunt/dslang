
package dslang.functor;

import org.junit.Assert;
import org.junit.Test;

import dslang.monad.wrapper.OptionM;

public class LazyFunctorTest {

    @Test
    public void test() {
        LazyFunctor<OptionM.t, Integer> lf = LazyFunctor.lift(OptionM.of(4));
        LazyFunctor<OptionM.t, String> lfs = lf.map(i -> "#" + i).map(s -> "val = " + s);
        OptionM<String> os = (OptionM<String>) lfs.run();
        Assert.assertEquals("val = #4", os.get());
    }

}
