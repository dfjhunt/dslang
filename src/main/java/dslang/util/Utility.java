
package dslang.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import dslang.monad.Monad;
import dslang.monad.wrapper.ListM;

public class Utility {
    public static <A> Stream<A> iterator2Stream(Iterator<A> it) {
        Iterable<A> iterable = () -> it;
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    @SuppressWarnings("unchecked")
    public static <M extends Monad<X, Y>, N extends Monad<X, List<Y>>, X, Y> N traverse(Function<List<Y>, N> unit, List<M> list) {
        // create the empty list
        Monad<X, List<Y>> mList = unit.apply(new ArrayList<Y>());

        // for each monad, add the value to the list
        for (Monad<X, Y> m : list) {
            mList = mList.flatMap(l->m.map(x->{l.add(x);return l;}));
        }

        // return the list, this is an unchecked cast but should only fail if there are two monads that extend Monad<X,?> where
        // X is the same. That should never happen since X is supposed to be the Monad's class
        return (N) mList;
    }

    @SuppressWarnings("unchecked")
    public static <M extends Monad<X, Y>, N extends Monad<X, ListM<Y>>, X, Y> N traverse(Function<ListM<Y>, N> unit, ListM<M> list) {
        // create the empty list
        Monad<X, ListM<Y>> mList = unit.apply(new ListM<Y>());

        // for each monad, add the value to the list
        for (Monad<X, Y> m : list.unwrap()) {
            mList = mList.flatMap(l->m.map(x->{l.add(x);return l;}));
        }

        // return the list, this is an unchecked cast but should only fail if there are two monads that extend Monad<X,?> where
        // X is the same. That should never happen since X is supposed to be the Monad's class
        return (N) mList;
    }

    public static <X> ListM<X> flatten(ListM<ListM<X>> llx) {
        return llx.flatMap(x->x);
    }
}
