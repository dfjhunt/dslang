
package dslang.monad;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import dslang.functor.SplitFunctor;
import dslang.util.function.TriFunction;
import dslang.util.function.checked.CheckedBiFunction;
import dslang.util.function.checked.CheckedConsumer;
import dslang.util.function.checked.CheckedFunction;
import dslang.util.function.checked.CheckedSupplier;
import dslang.util.function.checked.CheckedTriFunction;

public class Try<T> implements Monad<Try<?>, T>, SplitFunctor<Exception, T> {

    private final Exception e;

    private final T value;

    public Try(T value) {
        this.value = value;
        this.e = null;
    }

    public Try(Exception e) {
        this.e = e;
        this.value = null;
    }

    public static <T> Try<T> exception(Exception e) {
        return new Try<>(e);
    }

    public static <T> Try<T> of(T value) {
        return new Try<T>(value);
    }

    public Exception getException() {
        return e;
    }

    @Override
    public <U> Try<U> unit(U u) {
        return of(u);
    }

    public T get() throws Exception {
        if (value == null) {
            throw e;
        }
        return value;
    }

    public boolean isException() {
        return e != null;
    }

    // TODO - find common code with TryT for catching exceptions
    public <U> Try<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        if (isException())
            return exception(e);
        else {
            try {
                return new Try<U>(mapper.apply(value));
            } catch (Exception ex) {
                return exception(ex);
            }
        }
    }

    @Override
    public <U> Try<U> flatMap(Function<? super T, ? extends Monad<Try<?>, U>> mapper) {
        Objects.requireNonNull(mapper);
        if (isException())
            return exception(e);
        else {
            return (Try<U>) mapper.apply(value);
        }
    }

    public T orElse(T other) {
        return !isException() ? value
            : other;
    }

    public T orElseGet(Supplier<? extends T> other) {
        return !isException() ? value
            : other.get();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Try)) {
            return false;
        }

        Try<?> other = (Try<?>) obj;
        return Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return e == null ? String.format("Try[%s]", value)
            : "Try.exception";
    }

    @Override
    public <C> Try<C> repair(Function<? super Exception, ? extends C> left, Function<? super T, ? extends C> right) {
        Objects.requireNonNull(left);
        Objects.requireNonNull(right);
        try {
            if (isException()) {
                return Try.of(left.apply(e));
            } else {
                return Try.of(right.apply(value));
            }
        } catch (Exception ex) {
            return exception(ex);
        }
    }

    /********** Exceptional Function Wrappers **************/

    public static <T, E extends Exception> Try<T> ofChecked(CheckedSupplier<T, E> thunk) {
        try {
            return of(thunk.get());
        } catch (Exception e) {
            return exception(e);
        }
    }

    public static <A, E extends Exception> Function<A, Try<Void>> check(CheckedConsumer<A, E> f) {
        return x -> Try.ofChecked((CheckedSupplier<Void, E>) () -> {
            f.accept(x);
            return null;
        });
    }

    public static <A, B, E extends Exception> Function<A, Try<B>> check(CheckedFunction<A, B, E> f) {
        return x -> Try.ofChecked((CheckedSupplier<B, E>) () -> f.apply(x));
    }

    public static <A, B, C, E extends Exception> BiFunction<A, B, Try<C>> check(CheckedBiFunction<A, B, C, E> f) {
        return (a, b) -> Try.ofChecked((CheckedSupplier<C, E>) () -> f.apply(a, b));
    }

    public static <A, B, C, D, E extends Exception> TriFunction<A, B, C, Try<D>> check(CheckedTriFunction<A, B, C, D, E> f) {
        return (a, b, c) -> Try.ofChecked((CheckedSupplier<D, E>) () -> f.apply(a, b, c));
    }

}