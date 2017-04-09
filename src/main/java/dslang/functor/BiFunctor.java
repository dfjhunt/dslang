package dslang.functor;

import java.util.function.Function;

public interface BiFunctor<F,A,B> {
	
	public <C,D> BiFunctor<F,C,D> bimap(Function<? super A, ? extends C> f1, Function<? super B, ? extends D> f2);
	
	@SuppressWarnings("unchecked")
	default public <C,N extends BiFunctor<F,C,B>> N first(Function<? super A, ? extends C> f){
		return (N)bimap(f, x->x);
	}
	
	@SuppressWarnings("unchecked")
	default public <D, N extends BiFunctor<F, A, D>> N second(Function<? super B, ? extends D> f){
		return (N)bimap(x->x, f);
	}
}
