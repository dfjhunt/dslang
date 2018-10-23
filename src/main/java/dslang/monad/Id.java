package dslang.monad;

import java.util.function.Function;

public class Id<A> implements Monad<Id.t, A> {

    public static class t{
        
    }
    
    A _a;
    
    public Id(A a){
        _a = a;
    }
    
    public A get(){
        return _a;
    }
    
    @Override
    public <U> Monad<Id.t, U> unit(U u) {
        return new Id<>(u);
    }

    @Override
    public <U> Id<U> flatMap(Function<? super A, ? extends Monad<Id.t, U>> mapper) {
        return (Id<U>)mapper.apply(_a);
    }
}
