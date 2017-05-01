package dslang.functor;

import java.util.function.Function;
import java.util.function.Supplier;

public interface Functor<A> {
	public <B> Functor<B> map(Function<? super A, ? extends B> mapper);
	
	public static <A, B> Supplier<B> map(Supplier<A> s, Function<A,B> mapper){
	    return ()->mapper.apply(s.get());
	}
}
