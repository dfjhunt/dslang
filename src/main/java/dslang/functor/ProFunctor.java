
package dslang.functor;

import java.util.function.Function;

public interface ProFunctor<B, C> {
    public <A, D> ProFunctor<A, D> dimap(Function<? super A, ? extends B> lMap, Function<? super C, ? extends D> rMap);

    default public <A> ProFunctor<A, C> lmap(Function<? super A, ? extends B> f) {
        return dimap(f, x -> x);
    }

    default public <D> ProFunctor<B, D> rmap(Function<? super C, ? extends D> f) {
        return dimap(x -> x, f);
    }

    public static <A, B, C, D>
        Function<A, D>
        dimap(Function<B, C> f, Function<? super A, ? extends B> lMap, Function<? super C, ? extends D> rMap) {
        return a -> rMap.apply(f.apply(lMap.apply(a)));
    }
}
