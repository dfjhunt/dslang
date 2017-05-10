
package dslang.comonad;

import java.util.function.Function;

import dslang.functor.Functor;

//M acts as my "this" type, it should be the type of the particular monad implementing
//this interface, for the sake of allowing map and flatMap to return the same type of 
//monad with a different inner type
public interface Comonad<M, T> extends Functor<T> {
    public T extract();

    /**
     * extend takes a function from this comonad type to a value of type U and returns an instance of this comonad type of type U 
     * CM<T> -> (CM<T> -> U) -> CM<U>
     * 
     * @param f - a function of type CM<T> -> U
     * @return 
     */
    public <U, V extends Comonad<M, T>> Comonad<M, U> extend(Function<? super V, ? extends U> f);

    public Comonad<M, Comonad<M, T>> duplicate();

    @Override
    public <U> Comonad<M, U> map(Function<? super T, ? extends U> mapper);

    // This is an unsafe cast but should only fail if there are more than one implementation of a Monad with the
    // same M which should never happen since M should be the monad implementation class.
    @SuppressWarnings("unchecked")
    default public M getM() {
        return (M) this;
    }
}
