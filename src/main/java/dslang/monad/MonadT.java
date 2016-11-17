package dslang.monad;


public interface MonadT<M extends Monad<M, ?>, T> extends Monad<M, T>{
}
