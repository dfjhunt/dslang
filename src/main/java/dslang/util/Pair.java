
package dslang.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Pair<A, B> {
    public final A _1;

    public final B _2;

    public Pair(A one, B two) {
        this._1 = one;
        this._2 = two;
    }

    static public <X, Y> Pair<X, Y> of(X x, Y y) {
        return new Pair<X, Y>(x, y);
    }

    public String toString() {
        return "Pair(" + _1 + ", " + _2 + ")";
    }

    public static final <A, B> Stream<Pair<A, B>> zip(Stream<A> aStream, Stream<B> bStream) {
        PairIterator<A, B> pi = new PairIterator<A, B>(aStream.iterator(), bStream.iterator());
        return Utility.iterator2Stream(pi);
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
            Iterator<A> left = new SplitPairIterator<A>(() -> leftIndex, () -> leftIndex++, (p) -> p._1);
            Iterator<B> right = new SplitPairIterator<B>(() -> rightIndex, () -> rightIndex++, (p) -> p._2);
            pair = Pair.of(Utility.iterator2Stream(left), Utility.iterator2Stream(right));
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

    public static void main(String args[]) {
        // infinite stream
        Stream<Integer> s1 = Stream.iterate(1, i -> i + 1);
        Stream<String> s2 = Stream.of("a", "b", "c");

        Stream<Pair<Integer, String>> s3 = Pair.zip(s1.map(x -> {
            System.out.println("1st map: " + x);
            return x;
        }), s2);

        Stream<Pair<Integer, String>> s4 = s3.map(x -> {
            System.out.println("2nd map: " + x);
            return x;
        });

        Pair<Stream<Integer>, Stream<String>> p = Pair.unzip(s4);

        System.out.println("zipped");
        p._1.forEach(System.out::println);
        p._2.forEach(System.out::println);
    }
}
