
package dslang.functor;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import dslang.monad.wrapper.ListM;

public class ListMTraversable implements Traversable<ListM<?>> {

    @Override
    public <M, A, B> Applicative<M, Functor<ListM<?>, B>> traverse(
        Function<Functor<ListM<?>, B>, Applicative<M, Functor<ListM<?>, B>>> unit,
        Function<A, Applicative<M, B>> map,
        Functor<ListM<?>, A> f) {
        
        Applicative<M, Functor<ListM<?>, B>> returnVal = unit.apply(new ListM<>());
        
        BiFunction<Applicative<M, Functor<ListM<?>, B>>, A, Applicative<M, Functor<ListM<?>, B>>> add = 
                        (ab, a)->{
                            return ab.map2(map.apply(a), (l, b)->{((ListM<B>)l).add(b);return l;});
                        };
        
        List<A> l = ((ListM<A>)f).unwrap();
        for(A a:l){
            returnVal = add.apply(returnVal, a);
        }
                        
        return returnVal;
    }

}
