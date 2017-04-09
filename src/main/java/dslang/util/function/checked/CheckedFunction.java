package dslang.util.function.checked;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface CheckedFunction<T, R, E extends Exception> {
   R apply(T t) throws E;
   
   default public <V> CheckedFunction<T, V, E> andThen(Function<? super R, ? extends V> after){
       Objects.requireNonNull(after);
       return t->after.apply(apply(t));
   }
}
