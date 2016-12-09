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
}
