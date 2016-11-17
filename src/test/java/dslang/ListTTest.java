package dslang;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.junit.Assert;
import org.junit.Test;

import dslang.monad.transformer.ListT;
import dslang.monad.wrapper.OptionM;

public class ListTTest {

    @Test
    public void test() {
        ListT<OptionM<?>, Integer> listT = new ListT<OptionM<?>, Integer>(OptionM.sunit(Arrays.asList(1, 2, 3)));
        
        Function<Integer, List<Integer>> f = x->Arrays.asList(x, 2*x);
        
        Function<Integer, ListT<OptionM<?>, Integer>> f2 = f.andThen(OptionM::sunit).andThen(ListT::new);
        
        List<Integer> l = OptionM.unwrap(listT.flatMap(f2).run()).get();
        
        
        Assert.assertEquals(l, Arrays.asList(1, 2, 2, 4, 3, 6));
    }

}
