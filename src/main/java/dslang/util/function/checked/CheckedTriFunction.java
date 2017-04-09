package dslang.util.function.checked;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface CheckedTriFunction<A, B, C, R, E extends Exception> {

    R apply(A a, B b, C c) throws E;

    default <V> CheckedTriFunction<A, B, C, V,E> andThen(Function<? super R, ? extends V> after) throws E{
      Objects.requireNonNull(after);
      return (A a, B b, C c) -> after.apply(apply(a, b, c));
    }
  }