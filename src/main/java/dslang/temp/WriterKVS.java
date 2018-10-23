
package dslang.temp;

import java.util.HashMap;
import java.util.Map;

import dslang.monad.Monad;
import dslang.monad.Writer;
import dslang.monad.Writer.t;
import dslang.monad.wrapper.OptionM;

public class WriterKVS implements KeyValueStore<Writer.t<String>> {

    Map<Object, Object> store = new HashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public <K, V> Monad<Writer.t<String>, OptionM<V>> get(K key) {
        return new Writer<>("KVS.get " + key + "=" + store.get(key), OptionM.of((V) store.get(key)));
    }

    @Override
    public <K, V> Monad<Writer.t<String>, Boolean> put(K key, V value) {
        store.put(key, value);
        return new Writer<>("KVS.put " + key + ":" + value, true);
    }

    @Override
    public <A> Monad<t<String>, A> create(A a) {
        return new Writer<>("", a);
    }

}
