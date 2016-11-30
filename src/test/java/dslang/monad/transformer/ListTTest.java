package dslang.monad.transformer;

import java.util.Arrays;
import java.util.function.Function;

import org.junit.Assert;
import org.junit.Test;

import dslang.monad.wrapper.ListM;
import dslang.monad.wrapper.OptionM;

public class ListTTest {
    
    @Test
    public void test() {
        ListT<OptionM<?>, Integer> listT = new ListT<OptionM<?>, Integer>(OptionM.sunit(ListM.of(Arrays.asList(1, 2, 3))));
        
        Function<Integer, ListM<Integer>> f = x->ListM.of(Arrays.asList(x, 2*x));
        
        Function<Integer, ListT<OptionM<?>, Integer>> f2 = f.andThen(OptionM::sunit).andThen(ListT<OptionM<?>, Integer>::new);
        
        ListM<Integer> l = OptionM.unwrap(listT.flatMap(f2).run()).get();
        
        
        Assert.assertEquals(Arrays.asList(1, 2, 2, 4, 3, 6), l.unwrap());
    }

}
