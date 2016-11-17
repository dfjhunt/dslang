package dslang.util.function;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class Fluent {
    public static <U> UnaryOperator<U> of(Consumer<U> cons) {
        return x -> {
            cons.accept(x);
            return x;
        };
    }
    
    public static <X, Y> BiFunction<X, Y, X> ofBC(BiConsumer<X, Y> bicons) {
        return (x, y) -> {
            bicons.accept(x, y);
            return x;
        };
    }
    
    public static <X, Y,R> BiFunction<X, Y, X> ofBF(BiFunction<X, Y,R> bifunc) {
        return (x, y) -> {
            bifunc.apply(x, y);
            return x;
        };
    }
}
