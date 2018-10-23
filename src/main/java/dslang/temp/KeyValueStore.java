package dslang.temp;

import dslang.monad.Monad;
import dslang.monad.wrapper.OptionM;

public interface KeyValueStore<M> {
    public <K,V> Monad<M, OptionM<V>> get(K key);
    public <K,V> Monad<M, Boolean> put(K key, V value);
    
    public <A> Monad<M,A> create(A a);
}
