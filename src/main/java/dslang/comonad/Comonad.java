
package dslang.comonad;

import java.util.function.Function;

//M acts as my "this" type, it should be the type of the particular monad implementing
//this interface, for the sake of allowing map and flatMap to return the same type of 
//monad with a different inner type
public interface Comonad<M, T> {
    public T extract();
    
    public <U>  Comonad<M,U> extend(Function<Comonad<M, T>, U> f);
    
    public Comonad<M, Comonad<M,T>> duplicate();

    //This is an unsafe cast but should only fail if there are more than one implementation of a Monad with the
    //same M which should never happen since M should be the monad implementation class.
    @SuppressWarnings("unchecked")
    default public M getM() {
        return (M) this;
    }
}
