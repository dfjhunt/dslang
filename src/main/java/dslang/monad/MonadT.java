package dslang.monad;

public interface MonadT<M extends Monad<M, ?>, N, T, MT> extends Monad<M, T>{
    
    public  Monad<N, Monad<MT, T>> lift();
}
