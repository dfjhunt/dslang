package dslang.monad;

import java.util.function.Function;

public class Reader<E, T> implements Monad<Reader.t<E>, T> {
    public static class t<E>{
    }
    
    Function<E, T> _f;
    
    public Reader(Function<E,T> f){
        _f = f;
    }
    
    public static <C, X> Reader<C, X> of(Function<C,X> f){
        return new Reader<>(f);
    }
    
    public Function<E,T> read(){
        return _f;
    }
    
    public static <C> Reader<C,C> ask(){
        return new Reader<>(x->x);
    }
    
    public T run(E e){
        return _f.apply(e);
    }
    
    @Override
    public <U> Reader<E,U> unit(U u) {
        return new Reader<>(x->u);
    }
    
    public static <E, U> Reader<E,U> sunit(U u) {
        return new Reader<>(x->u);
    }

    public static <I,A> Reader<I, A> wrap(Monad<Reader.t<I>, A> m){
        return (Reader<I,A>)m;
    }
    
    @Override
    public <U> Reader<E,U> map(Function<? super T, ? extends U> mapper) {
        return new Reader<>(_f.andThen(mapper));
    }

    @Override
    public <U> Reader<E,U> flatMap(Function<? super T, ? extends Monad<Reader.t<E>, U>> mapper) {
        return new Reader<>(e->((Reader<E, U>)mapper.apply(_f.apply(e))).read().apply(e));
    }
    
}
