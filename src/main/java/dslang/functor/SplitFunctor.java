
package dslang.functor;

import java.util.function.Function;

import dslang.monad.Either;

public interface SplitFunctor<A, B> {
    public <C> SplitFunctor<A, C> repair(Function<? super A, ? extends C> left, Function<? super B, ? extends C> right);

    default SplitFunctor<A, B> repair(Function<? super A, ? extends B> left) {
        return repair(left, x -> x);
    }

    default public SplitFunctor<A, Either<A, B>> toEither() {
        return repair(Either::left, Either::right);
    }
}
