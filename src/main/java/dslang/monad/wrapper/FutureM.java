package dslang.monad.wrapper;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import dslang.monad.Monad;
import dslang.monad.MonadWrapper;

public class FutureM<T> implements MonadWrapper<FutureM<?>, T, CompletableFuture<T>> {

    CompletableFuture<T> myFuture = null;

    public FutureM(CompletableFuture<T> future) {
        myFuture = future;
    }

    static public <S> FutureM<S> of(CompletableFuture<S> future){
        return new FutureM<S>(future);
    }
    
    @Override
    public <U> FutureM<U> unit(U u) {
        return sunit(u);
    }
    
    static public <U> FutureM<U> sunit(U u) {
        return new FutureM<U>(CompletableFuture.completedFuture(u));
    }
    
    @Override
    public CompletableFuture<T> unwrap() {
        return myFuture;
    } 

    @Override
    public <U> FutureM<U> map(Function<? super T, ? extends U> mapper) {
        return new FutureM<U>(myFuture.thenApply(mapper));
    }

    @Override
    public <U> FutureM<U> flatMap(Function<? super T, ? extends Monad<FutureM<?>, U>> mapper) {
        Function<? super T, CompletableFuture<U>> newMapper = mapper.andThen(o -> ((FutureM<U>) o).unwrap());
        return new FutureM<U>(myFuture.thenCompose(newMapper));
    }

}
