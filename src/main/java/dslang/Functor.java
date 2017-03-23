package dslang;

import java.util.function.Function;

public interface Functor<F, A> {
	public <B> Functor<F, B> map(Function<? super A, ? extends B> mapper);
}
