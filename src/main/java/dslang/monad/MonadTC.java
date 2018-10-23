package dslang.monad;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface MonadTC<M>{
    public <A> Monad<M, A> unit(A a);
    
    public <A,B> Monad<M,B> flatMap(Function<A, Monad<M,B>> f, Monad<M,A> ma);
    
    default public <A,B> Monad<M,B> map(Function<A,B> f, Monad<M,A> ma){
        Function<A, Monad<M,B>> f2 = f.andThen(this::unit);
        return ma.flatMap(f2);
    }
    
    default public <A,B,C> Monad<M, C> map2(BiFunction<A,B,C> f, Monad<M,A> ma, Monad<M,B> mb){
        return ma.flatMap(a->mb.map(b->f.apply(a, b)));
    }
}
