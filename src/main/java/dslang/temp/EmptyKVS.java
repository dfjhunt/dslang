package dslang.temp;

import java.util.Map;
import java.util.Optional;

import dslang.monad.Monad;
import dslang.monad.Reader;
import dslang.monad.Reader.t;
import dslang.monad.wrapper.OptionM;

public class EmptyKVS implements KeyValueStore<Reader.t<Map<Object, Object>>> {

    @SuppressWarnings("unchecked")
    @Override
    public <K, V> Monad<t<Map<Object, Object>>, OptionM<V>> get(K key) {
        return new Reader<>(m->new OptionM<V>(Optional.ofNullable((V)m.get(key))));
    }

    @Override
    public <K, V> Monad<t<Map<Object, Object>>, Boolean> put(K key, V value) {
        return new Reader<>(m->{m.put(key, value);return true;});
    }

    @Override
    public <A> Monad<t<Map<Object, Object>>, A> create(A a) {
        return Reader.sunit(a);
    }

}
