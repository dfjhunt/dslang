
package dslang.functor;

import java.util.function.Function;

public interface Traversable<F> {
    public <M, A, B> Applicative<M, Functor<F, B>> traverse(
        Function<Functor<F, B>, Applicative<M, Functor<F, B>>> unit,
        Function<A, Applicative<M, B>> map,
        Functor<F, A> f);
}
