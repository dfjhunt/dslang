package dslang.functor;

import java.util.function.Function;
import java.util.function.Supplier;

public interface Functor<F, A> {
	public <B> Functor<F, B> map(Function<? super A, ? extends B> mapper);
	
	public static <A, B> Supplier<B> map(Supplier<A> s, Function<A,B> mapper){
	    return ()->mapper.apply(s.get());
	}
}
