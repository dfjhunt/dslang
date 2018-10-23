
package dslang.temp.simple;

import java.util.function.Function;

public interface Functor<F, A> extends Wrap<F, A> {
    public <B> Functor<F, B> fmap(Function<A, B> f);
}
