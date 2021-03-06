package dslang.monad.wrapper;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class ListMTest {
    
    @Test
    public void unwrapTest(){
        List<Integer> li = Arrays.asList(1, 2, 3);
        ListM<Integer> lm = ListM.of(li);
        Assert.assertEquals(li, lm.unwrap());
    }
    
    @Test
    public void mapTest(){
        List<Integer> li = Arrays.asList(1, 2, 3);
        ListM<Integer> lm = ListM.of(li);
        lm = lm.map(x->x+1);
        Assert.assertEquals(Arrays.asList(2, 3, 4), lm.unwrap());
    }
    
    @Test
    public void traverseTest(){
        ListM<Integer> lm = ListM.of(1, 2, 3);
        OptionM<ListM<String>> oli = lm.traverse(OptionM::sunit, i->OptionM.of(""+i));
        Assert.assertEquals(Arrays.asList("1","2","3"), oli.get().unwrap());
    }
}
