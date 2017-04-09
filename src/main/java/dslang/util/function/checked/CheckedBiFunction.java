package dslang.util.function.checked;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface CheckedBiFunction<A, B, R, E extends Exception> {
    R apply(A a, B b) throws E;

    default public <V> CheckedBiFunction<A, B, V, E> andThen(Function<? super R, ? extends V> after){
        Objects.requireNonNull(after);
        return (a,b)->after.apply(apply(a,b));
    }
}
