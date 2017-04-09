package dslang.functor;

import java.util.function.Consumer;
import java.util.function.Function;

public interface ContraFunctor<F,B> {
	public <A> ContraFunctor<F,A> contraMap(Function<?super A, ? extends B> f); 
	
	public static <S,T> Consumer<S> contraMap(Consumer<T> c, Function<? super S, ? extends T> f){
	    return s->c.accept(f.apply(s));
	}
}
