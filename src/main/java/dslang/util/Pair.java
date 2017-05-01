
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

    public Pair(A one, B two) {
        this._left = one;
        this._right = two;
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
        PairIterator<A, B> pi = new PairIterator<A, B>(aStream.iterator(), bStream.iterator());
        return StreamUtil.fromIterator(pi);
    }

    public static final <A, B> Pair<Stream<A>, Stream<B>> unzip(Stream<Pair<A, B>> stream) {
        return PairIteratorSplitter.splitPairs(stream);
    }

    static class PairIterator<A, B> implements Iterator<Pair<A, B>> {
        private final Iterator<A> aIterator;

        private final Iterator<B> bIterator;

        public PairIterator(Iterator<A> aIterator, Iterator<B> bIterator) {
            this.aIterator = aIterator;
            this.bIterator = bIterator;
        }

        @Override
        public boolean hasNext() {
            return aIterator.hasNext() && bIterator.hasNext();
        }

        @Override
        public Pair<A, B> next() {
            if (!aIterator.hasNext() || !bIterator.hasNext())
                throw new NoSuchElementException();
            return Pair.of(aIterator.next(), bIterator.next());
        }
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
