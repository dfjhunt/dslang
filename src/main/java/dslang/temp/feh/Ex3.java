package dslang.temp.feh;

import dslang.temp.simple.Wrap;

public class Ex3 {
    public <T> Wrap<T, Integer> ex3_1(ExprWBool<T> e){
        return e.add(e.num(2), e.num(3));
    }
    
    public <T> Wrap<T, Boolean> ex3_2(ExprWBool<T> e){
        return e.and(e.bool(true), e.bool(false));
    }
}
