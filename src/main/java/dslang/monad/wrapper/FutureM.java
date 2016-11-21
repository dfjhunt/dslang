package dslang.monad.wrapper;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import dslang.monad.Monad;

public class FutureM<T> implements Monad<FutureM<?>, T> {

    CompletableFuture<T> myFuture = null;

    public CompletableFuture<T> unwrap() {
        return myFuture;
    }

    public FutureM(CompletableFuture<T> future) {
        myFuture = future;
    }

    static public <S> FutureM<S> of(CompletableFuture<S> future){
        return new FutureM<S>(future);
    }

    @Override
    public <U> FutureM<U> unit(U u) {
        return new FutureM<U>(CompletableFuture.completedFuture(u));
    }
    
    static public <U> FutureM<U> sunit(U u) {
        return new FutureM<U>(CompletableFuture.completedFuture(u));
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
