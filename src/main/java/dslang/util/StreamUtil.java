package dslang.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamUtil {
    public static <T> Stream<T> recurse(final T seed, final Function<? super T, ? extends Boolean> hasNext,
                                        final Function<? super T, ? extends T> getNext) {
        Objects.requireNonNull(seed);

        final Iterator<T> iterator = new Iterator<T>() {
            T t = (T) null;

            @Override
            public boolean hasNext() {
                return (t == null) || hasNext.apply(t);
            }

            @Override
            public T next() {
                if(!hasNext())
                    throw new NoSuchElementException();
                return t = (t == null) ? seed : getNext.apply(t);
            }
        };
        return fromIterator(iterator);
    }

    public static <T> Stream<T> recurseTilNull(T seed, Function<T,T> next){
        return recurse(seed, t->next.apply(t)!=null, next);
    }
    
    public static <T> Stream<T> fromIterator(final Iterator<T> i) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(i, Spliterator.ORDERED | Spliterator.IMMUTABLE), false);
    }

    public static <S extends T, T> Function<T, Stream<S>> cast(Class<S> cls) {
        return t -> {
            if (!cls.isInstance(t))
                return Stream.empty();
            else
                return Stream.of(cls.cast(t));
        };
    }
    
    public static void main(String args[]){
        StreamUtil.recurse(1, i->(i<5), i->i+1).forEach(i->System.out.println(i));
    }
}
