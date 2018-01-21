package dslang.monad.wrapper;

import org.junit.Assert;
import org.junit.Test;

public class OptionMTest {

    @Test
    public void mapTest(){
        OptionM<Integer> opt1 = OptionM.of(1);
        Assert.assertEquals(true, opt1.map(x->x==1).get());
    }
    
    @Test
    public void map2Test(){
        OptionM<Integer> opt1 = OptionM.of(1);
        OptionM<Integer> opt2 = OptionM.of(2);
        Assert.assertEquals((Integer)3, opt1.map2(opt2, (a, b)->a+b).get());
    }
}
