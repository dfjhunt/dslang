
package dslang.temp;

import java.util.HashMap;
import java.util.Map;

import dslang.monad.Monad;
import dslang.monad.transformer.OptionT;
import dslang.monad.wrapper.FutureM;
import dslang.monad.wrapper.OptionM;

public class ComplexKVS implements KeyValueStore<OptionT<FutureM.t, ?>> {

    Map<Object, Object> store = new HashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public <K, V> Monad<OptionT<FutureM.t, ?>, OptionM<V>> get(K key) {
        if (store.containsKey(key))
            return create(OptionM.of((V)store.get(key)));
        else
            return new OptionT<>(FutureM.sunit(OptionM.empty()));
    }

    @Override
    public <K, V> Monad<OptionT<FutureM.t, ?>, Boolean> put(K key, V value) {
        store.put(key, value);
        return create(true);
    }

    @Override
    public <A> Monad<OptionT<FutureM.t, ?>, A> create(A a) {
        return OptionT.of(FutureM.sunit(a));
    }

}
