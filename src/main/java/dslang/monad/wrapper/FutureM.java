
package dslang.monad.wrapper;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

import dslang.functor.SplitFunctor;
import dslang.monad.Monad;
import dslang.monad.MonadWrapper;

public class FutureM<T> implements MonadWrapper<FutureM.t, T, CompletableFuture<T>>, SplitFunctor<Throwable, T> {

    public static class t{
    }
    
    CompletableFuture<T> myFuture = null;

    public FutureM(CompletableFuture<T> future) {
        myFuture = future;
    }

    static public <S> FutureM<S> of(CompletableFuture<S> future) {
        return new FutureM<>(future);
    }

    public T get() throws ExecutionException, InterruptedException {
        return myFuture.get();
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
    public <U> FutureM<U> flatMap(Function<? super T, ? extends Monad<FutureM.t, U>> mapper) {
        Function<? super T, CompletableFuture<U>> newMapper = mapper.andThen(o -> ((FutureM<U>) o).unwrap());
        return new FutureM<U>(myFuture.thenCompose(newMapper));
    }

    @Override
    public <C> FutureM<C> repair(Function<? super Throwable, ? extends C> left, Function<? super T, ? extends C> right) {
        Objects.requireNonNull(left);
        Objects.requireNonNull(right);
        return new FutureM<C>(myFuture.handle((value, throwable) -> {
            if (throwable != null)
                return left.apply(throwable);
            else
                return right.apply(value);
        }));
    }

}
