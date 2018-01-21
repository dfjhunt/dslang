package dslang.monad;

import java.util.function.Function;

public class Reader<E, T> implements Monad<Reader<E,?>, T> {

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
    
    @Override
    public <U> Monad<Reader<E,?>, U> unit(U u) {
        return new Reader<>(x->u);
    }

    @Override
    public <U> Monad<Reader<E,?>, U> map(Function<? super T, ? extends U> mapper) {
        return new Reader<>(_f.andThen(mapper));
    }

    @Override
    public <U> Reader<E,U> flatMap(Function<? super T, ? extends Monad<Reader<E,?>, U>> mapper) {
        return new Reader<>(e->((Reader<E, U>)mapper.apply(_f.apply(e))).read().apply(e));
    }
    
}
