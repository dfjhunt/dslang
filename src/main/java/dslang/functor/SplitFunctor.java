package dslang.functor;

import java.util.function.Function;

import dslang.monad.Either;

public interface SplitFunctor<F,A,B> {
    public <C> SplitFunctor<F,A,C> repair(Function<? super A, ? extends C> left, Function<? super B, ? extends C> right);
    
    default SplitFunctor<F, A, B> repair(Function<? super A, ? extends B> left){
        return repair(left, x->x);
    }
    
    default public  SplitFunctor<F, A, Either<A,B>> toEither(){
        return repair(Either::left, Either::right);
    }
}
