
package dslang.util;

import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamUtil {
    public static <T> Function<T, Stream<T>> getRecurser(
        final Function<? super T, ? extends Boolean> hasNext,
        final Function<? super T, ? extends T> getNext) {
        return seed -> {
            Objects.requireNonNull(seed);

            final Iterator<T> iterator = new Iterator<T>() {
                T t = (T) null;

                @Override
                public boolean hasNext() {
                    return (t == null) || hasNext.apply(t);
                }

                @Override
                public T next() {
                    return t = (t == null) ? seed
                        : getNext.apply(t);
                }
            };
            return fromIterator(iterator);
        };
    }

    // creates a stream from an iterator defined by hasNext and getNext
    public static <T> Stream<T> recurse(
        final T seed,
        final Function<? super T, ? extends Boolean> hasNext,
        final Function<? super T, ? extends T> getNext) {
        return StreamUtil.<T>getRecurser(hasNext, getNext).apply(seed);
    }

    public static <T> Stream<T> recurseTilNull(T seed, Function<T, T> next) {
        return recurse(seed, t -> next.apply(t) != null, next);
    }

    public static <S> Stream<S> recurse(S seed, Function<S, Stream<S>> expand) {
        Supplier<Stream<S>> temp = () -> expand.apply(seed).filter(x -> x != null).flatMap(x -> recurse(x, expand));
        return concatLazyStreams(() -> Stream.of(seed), temp);
    }

    /**
     * Generates a stream from seed using gen as long as hasNext is true. gen returns the next element of the stream 
     * and the next seed to use.
     * 
     * @param hasNext true if there are more elements to be generated
     * @param gen function that generates a value and a new seed from a seed
     * @param seed starting seed for generating the stream
     * @return a lazy stream generator of T
     */
    public static <S, T> Stream<T> unfold(Function<S, Boolean> hasNext, Function<S, Pair<T, S>> gen, S seed) {
        if (hasNext.apply(seed)) {
            Stream<Pair<T, S>> str = recurse(gen.apply(seed), p -> hasNext.apply(p.right()), p -> gen.apply(p.right()));
            return str.map(p -> p.left());
        } else {
            return Stream.empty();
        }
    }

    public static <T> Stream<T> fromIterator(final Iterator<T> i) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(i, Spliterator.ORDERED | Spliterator.IMMUTABLE), false);
    }

    @SafeVarargs
    public static <T> Stream<T> concatLazyStreams(final Supplier<Stream<T>>... s) {
        return concatLazyStreams(Stream.of(s));
    }

    public static <T> Stream<T> concatLazyStreams(final Stream<Supplier<Stream<T>>> s) {
        return s.flatMap(x -> x.get());
    }

    public static <S extends T, T> Function<T, Stream<S>> cast(Class<S> cls) {
        return t -> {
            if (!cls.isInstance(t))
                return Stream.empty();
            else
                return Stream.of(cls.cast(t));
        };
    }
}
