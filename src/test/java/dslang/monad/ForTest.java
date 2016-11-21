package dslang.monad;

import org.junit.Assert;
import org.junit.Test;

import dslang.monad.wrapper.OptionM;

public class ForTest {

    @Test
    public void testForBiFunction() {
        OptionM<Integer> opt = For.of(OptionM.sunit(3), OptionM.sunit(5), (x,y)->x+y);
        
        Assert.assertTrue(opt.isPresent());
        Assert.assertEquals((int)opt.get(), (int)8);
    }
    
    @Test
    public void testForBiFunction2() {
        OptionM<Integer> emp = OptionM.empty();
        OptionM<Integer> opt = For.of(emp, OptionM.sunit(5), (x,y)->x+y);
        
        Assert.assertFalse(opt.isPresent());
    }
    
    @Test
    public void testForTriFunction() {
        OptionM<Integer> opt = For.of(OptionM.sunit(3), OptionM.sunit(5), OptionM.sunit(2), (x,y,z)->x+y+z);
        
        Assert.assertTrue(opt.isPresent());
        Assert.assertEquals((int)opt.get(), (int)10);
    }
    
    @Test
    public void testForTriFunction2() {
        OptionM<Integer> emp = OptionM.empty();
        OptionM<Integer> opt = For.of(emp, OptionM.sunit(5), OptionM.sunit(2), (x,y,z)->x+y+z);
        
        Assert.assertFalse(opt.isPresent());
    }

}