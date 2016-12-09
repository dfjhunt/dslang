
package dslang.monad.transformer;

import org.junit.Assert;
import org.junit.Test;

import dslang.monad.Try;
import dslang.monad.wrapper.FutureM;
import dslang.monad.wrapper.OptionM;

public class TryTTest {

    public static <X> OptionT<TryT<FutureM<?>, ?>, X> helper(X val) {
        return OptionT.of(TryT.of(FutureM.sunit(val)));
    }

    class Probe<X> {
        X value;

        public Probe() {
        }

        public Probe(X defVal) {
            value = defVal;
        }

        public X set(X x) {
            value = x;
            return x;
        }

        public X get() {
            return value;
        }
    }

    @Test
    public void test2LayerTransformerMap() {
        OptionT<TryT<FutureM<?>, ?>, Integer> temp = helper(3);
        temp = temp.map(x -> x + 1);
        Probe<Integer> p = new Probe<>();
        temp = temp.map(p::set);
        Assert.assertEquals((int) 4, (int) p.get());

    }

    @Test
    public void test2LayerTransformerMapNull() {
        OptionT<TryT<FutureM<?>, ?>, Integer> temp = helper(3);
        temp = temp.map(x -> null);
        Probe<Integer> p = new Probe<>(-1);
        temp = temp.map(p::set);
        Assert.assertEquals((int) -1, (int) p.get());
        
        Probe<Boolean> p2 = new Probe<>();
        TryT<FutureM<?>, Boolean> tempTry = temp.lift(OptionM::isPresent);
        tempTry.map(p2::set);
        Assert.assertFalse(p2.get());
    }

    @Test
    public void test2LayerTransformerFlatMap() {
        OptionT<TryT<FutureM<?>, ?>, Integer> temp = helper(3);
        temp = temp.flatMap(x -> helper(x + 2));
        Probe<Integer> p = new Probe<>();
        temp = temp.map(p::set);
        Assert.assertEquals((int) 5, (int) p.get());
    }

    @Test
    public void test2LayerTransformerExceptionMap(){
        OptionT<TryT<FutureM<?>, ?>, Integer> temp = helper(3);
        temp = temp.map(x->{throw new RuntimeException();});
        temp.map(x->x+3);
        
        Probe<Integer> p = new Probe<>(-1);
        temp = temp.map(p::set);
        Assert.assertEquals((int) -1, (int) p.get());
        
        Probe<Boolean> p2 = new Probe<>();
        temp.liftM().lift(Try::isException).map(p2::set);
        Assert.assertTrue(p2.get());
        
    }
    
}
