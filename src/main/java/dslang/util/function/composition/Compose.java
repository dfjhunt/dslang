package dslang.util.function.composition;

import java.util.function.BiFunction;
import java.util.function.Function;

import dslang.util.function.TriFunction;

public class Compose {
    
    //BiFunction
    
    static public <A, B, C, D, R> TriFunction<A, C, D, R> of(BiFunction<A, B, R> fAB, Hole h, BiFunction<C, D, B> fCD) {
        return (A a, C c, D d) -> fAB.apply(a, fCD.apply(c, d));
    }

    static public <A, B, C, D, R> TriFunction<C, D, B, R> of(BiFunction<A, B, R> fAB, BiFunction<C, D, A> fCD, Hole h) {
        return (C c, D d, B b) -> fAB.apply(fCD.apply(c, d), b);
    }
    
    static public <A, B, C, R> BiFunction<C, B, R> of(BiFunction<A, B, R> fAB, Function<C,  A> fC, Hole h) {
        return (C c, B b) -> fAB.apply(fC.apply(c), b);
    }
    
    static public <A, B, C, R> BiFunction<A, C, R> of(BiFunction<A, B, R> fAB,  Hole h, Function<C,  B> fC) {
        return (A a, C c) -> fAB.apply(a, fC.apply(c));
    }
    
    static public <A, B, C, D, R> BiFunction<C, D, R> of(BiFunction<A, B, R> fAB,  Function<C, A> fC, Function<D,  B> fD) {
        return (C c, D d) -> fAB.apply(fC.apply(c), fD.apply(d));
    }
}
