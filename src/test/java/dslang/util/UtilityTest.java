package dslang.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import dslang.monad.wrapper.ListM;
import dslang.monad.wrapper.OptionM;

public class UtilityTest {

    @Test
    public void testListTraverse() {
        List<OptionM<Integer>> lo = new ArrayList<>();
        lo.add(OptionM.sunit(1));
        lo.add(OptionM.sunit(2));
        lo.add(OptionM.sunit(3));

        OptionM<List<Integer>> omli = Utility.sequence(OptionM::sunit, lo);
        
        Assert.assertEquals(omli.get(), Arrays.asList(1, 2, 3));
    }
    
    @Test
    public void testListMTraverse() {
        ListM<OptionM<Integer>> lo = new ListM<OptionM<Integer>>();
        lo.add(OptionM.sunit(1));
        lo.add(OptionM.sunit(2));
        lo.add(OptionM.sunit(3));

        OptionM<ListM<Integer>> omli = Utility.sequence(OptionM::sunit, lo);
        
        Assert.assertEquals(omli.get().unwrap(), Arrays.asList(1, 2, 3));
    }
    
    @Test
    public void testListMTraverseShortCircuit() {
        System.out.println("testSC");
        ListM<OptionM<Integer>> lo = new ListM<OptionM<Integer>>();
        lo.add(OptionM.empty());
        lo.add(OptionM.sunit(2));
        lo.add(OptionM.sunit(3));

        OptionM<ListM<Integer>> omli = Utility.sequence(OptionM::sunit, lo);
        
        System.out.println(omli);
        
        Assert.assertFalse(omli.isPresent());
    }
    
    @Test
    public void testFlatten() {
        ListM<ListM<Integer>> lo = new ListM<ListM<Integer>>();
        lo.add(new ListM<Integer>(Arrays.asList(1, 2, 3)));
        lo.add(new ListM<Integer>(Arrays.asList(4,5,6)));

        ListM<Integer> li = Utility.flatten(lo);
        
        Assert.assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6),li.unwrap());
    }

}
