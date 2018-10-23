package dslang.temp.simple;

public class IdWrapper<A> implements Wrap<IdWrapper<?>, A>{
    A _a;
    
    public IdWrapper(A a){
        _a = a;
    }
    
    public A get(){
        return _a;
    }
}
