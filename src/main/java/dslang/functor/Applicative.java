package dslang.functor;

import java.util.function.BiFunction;

public interface Applicative<F, A> extends Functor<F, A>{
    public <B,C> Applicative<F,C> map2(Applicative<F,B> apb, BiFunction<A,B,C> f);

}
