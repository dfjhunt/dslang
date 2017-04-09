package dslang.util.function.composition;

import java.util.function.BiFunction;
import java.util.function.Function;

import dslang.util.function.TriFunction;

public class Partial {

    static public <A, B, R> Function<A, Function<B, R>> curry(BiFunction<A, B, R> bi) {
        return (A a) -> (B b)-> bi.apply(a, b);
    }
    
    static public <A, B, R> Function<B, R> of(BiFunction<A, B, R> bi, A a, Hole h) {
        return (B b) -> bi.apply(a, b);
    }

    static public <A, B, R> Function<A, R> of(BiFunction<A, B, R> bi, Hole h, B b) {
        return (A a) -> bi.apply(a, b);
    }

    /************** TriFunctions ***************/
    static public <A, B, C, R> Function<A, Function<B, Function<C, R>>> curry(TriFunction<A, B, C, R> tri) {
        return (A a) -> (B b) -> (C c) -> tri.apply(a, b, c);
    }
    
    // f(_, b, c)
    static public <A, B, C, R> Function<A, R> of(TriFunction<A, B, C, R> f, Hole h, B b, C c) {
        return (A a) -> f.apply(a, b, c);
    }

    // f(a, _, c)
    static public <A, B, C, R> Function<B, R> of(TriFunction<A, B, C, R> f, A a, Hole h, C c) {
        return (B b) -> f.apply(a, b, c);
    }

    // F(a, b, _)
    static public <A, B, C, R> Function<C, R> of(TriFunction<A, B, C, R> f, A a, B b, Hole h) {
        return (C c) -> f.apply(a, b, c);
    }

    // f(a, _, _)
    static public <A, B, C, R> BiFunction<B, C, R> of(TriFunction<A, B, C, R> f, A a, Hole h, Hole h1) {
        return (B b, C c) -> f.apply(a, b, c);
    }

    // f(_, b, _)
    static public <A, B, C, R> BiFunction<A, C, R> of(TriFunction<A, B, C, R> f, Hole h1, B b, Hole h) {
        return (A a, C c) -> f.apply(a, b, c);
    }

    // f(_, _, c)
    static public <A, B, C, R> BiFunction<A, B, R> of(TriFunction<A, B, C, R> f, Hole h1, Hole h, C c) {
        return (A a, B b) -> f.apply(a, b, c);
    }
}
