
package dslang.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import dslang.functor.BiFunctor;
import dslang.monad.Monad;

public class Pair<A, B> implements BiFunctor<A, B> {
    private final A _left;

    private final B _right;

    public Pair(A left, B right) {
        this._left = left;
        this._right = right;
    }

    public A left() {
        return _left;
    }

    public B right() {
        return _right;
    }

    static public <X, Y> Pair<X, Y> of(X x, Y y) {
        return new Pair<X, Y>(x, y);
    }

    public String toString() {
        return "Pair(" + _left + ", " + _right + ")";
    }

    public <X> X mapPair(BiFunction<A, B, X> f) {
        return f.apply(_left, _right);
    }

    public static final <A, B> Stream<Pair<A, B>> zip(Stream<A> aStream, Stream<B> bStream) {
        return zip(aStream.iterator(), bStream.iterator());
    }

    public static final <A, B> Stream<Pair<A, B>> zip(Iterable<A> aIterable, Iterable<B> bIterable) {
        return zip(aIterable.iterator(), bIterable.iterator());
    }

    public static final <A, B> Stream<Pair<A, B>> zip(Iterator<A> aIterator, Iterator<B> bIterator) {
        return zipWith(Pair::of, aIterator, bIterator);
    }
    
    public static final <A,B,C> Stream<C> zipWith(BiFunction<A,B,C> f, Iterator<A> aIterator, Iterator<B> bIterator){
        return StreamUtil.unfold(p -> p.left().hasNext() && p.right().hasNext(), //
            p -> Pair.of(f.apply(p.left().next(), p.right().next()), Pair.of(p.left(), p.right())),//
            Pair.of(aIterator, bIterator));
    }

    public static final <A, B> Pair<Stream<A>, Stream<B>> unzip(Stream<Pair<A, B>> stream) {
        return PairIteratorSplitter.splitPairs(stream);
    }

    static class PairIteratorSplitter<A, B> {
        int leftIndex = 0, rightIndex = 0;

        List<Pair<A, B>> pairs = new ArrayList<Pair<A, B>>();

        Pair<Stream<A>, Stream<B>> pair = null;

        Iterator<Pair<A, B>> _iterator = null;

        public PairIteratorSplitter(Stream<Pair<A, B>> str) {
            _iterator = str.iterator();
            Iterator<A> left = new SplitPairIterator<A>(() -> leftIndex, () -> leftIndex++, (p) -> p._left);
            Iterator<B> right = new SplitPairIterator<B>(() -> rightIndex, () -> rightIndex++, (p) -> p._right);
            pair = Pair.of(StreamUtil.fromIterator(left), StreamUtil.fromIterator(right));
        }

        static public <A, B> Pair<Stream<A>, Stream<B>> splitPairs(Stream<Pair<A, B>> str) {
            PairIteratorSplitter<A, B> splitter = new PairIteratorSplitter<A, B>(str);
            return splitter.pair;
        }

        class SplitPairIterator<C> implements Iterator<C> {
            Supplier<Integer> index;

            Supplier<Integer> incrementIndex;

            Function<Pair<A, B>, C> getValue;

            public SplitPairIterator(Supplier<Integer> index, Supplier<Integer> incrementIndex, Function<Pair<A, B>, C> getValue) {
                this.index = index;
                this.incrementIndex = incrementIndex;
                this.getValue = getValue;
            }

            @Override
            public boolean hasNext() {
                synchronized (pairs) {
                    return ((index.get() < pairs.size()) || (_iterator.hasNext()));
                }
            }

            @Override
            public C next() {
                synchronized (pairs) {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }

                    if (index.get() >= pairs.size()) {
                        pairs.add(_iterator.next());
                    }
                    return getValue.apply(pairs.get(incrementIndex.get()));
                }
            }
        }
    }

    /**
     * Turns a pair of monads into a monad of pair
     * 
     * @param pair - pair of two monads of the same monad-type
     * @return - monad of a pair
     */
    public static <M, A, B> Monad<M, Pair<A, B>> sequence(Pair<Monad<M, A>, Monad<M, B>> pair) {
        return pair._left.flatMap(a -> pair._right.map(b -> Pair.of(a, b)));
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        return (o != null) && (o instanceof Pair) && (Objects.deepEquals(((Pair<A, B>) o)._left, _left)) &&
                        (Objects.deepEquals(((Pair<A, B>) o)._right, _right));
    }

    @Override
    public int hashCode() {
        return Objects.hash(_left, _right);
    }

    @Override
    public <C, D> Pair<C, D> bimap(Function<? super A, ? extends C> f1, Function<? super B, ? extends D> f2) {
        return Pair.of(f1.apply(_left), f2.apply(_right));
    }
}
