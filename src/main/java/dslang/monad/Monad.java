
package dslang.monad;

import java.util.function.Function;

//M acts as my "this" type, it should be the type of the particular monad implementing
//this interface, for the sake of allowing map and flatMap to return the same type of 
//monad with a different inner type
public interface Monad<M, T> {
    public <U> Monad<M, U> unit(U u);

    public <U> Monad<M, U> map(Function<? super T, ? extends U> mapper);

    public <U> Monad<M, U> flatMap(Function<? super T, ? extends Monad<M, U>> mapper);

    //This is an unsafe cast but should only fail if there are more than one implementation of a Monad with the
    //same M which should never happen since M should be the monad implementation class.
    @SuppressWarnings("unchecked")
    default public M getM() {
        return (M) this;
    }
}
