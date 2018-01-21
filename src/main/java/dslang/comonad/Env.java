package dslang.comonad;

import java.util.function.Function;

public class Env<E,A> implements Comonad<Env<E,?>,A>{

    E _e;
    A _a;
    
    public Env(E e, A a){
        _e = e;
        _a = a;
    }

    public E ask(){
        return _e;
    }
    
    @Override
    public A extract() {
        return _a;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <U, V extends Comonad<Env<E, ?>, A>> Env<E,U> extend(Function<? super V, ? extends U> f) {
        return new Env<>(_e, f.apply((V)this));
    }

    @Override
    public <U> Comonad<Env<E, ?>, U> map(Function<? super A, ? extends U> mapper) {
        return new Env<>(_e, mapper.apply(_a));
    }
    

}
