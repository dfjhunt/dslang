
package dslang.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import dslang.monad.For;
import dslang.monad.Monad;
import dslang.monad.wrapper.OptionM;
import dslang.util.function.Fluent;

public class Utility {
    public static <A> Stream<A> iterator2Stream(Iterator<A> it) {
        Iterable<A> iterable = () -> it;
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    @SuppressWarnings("unchecked")
    public static <M extends Monad<X, Y>, N extends Monad<X, List<Y>>, X, Y> N traverse(Function<List<Y>, N> unit, List<M> list) {
        // create the empty list
        Monad<X, List<Y>> mList = unit.apply(new ArrayList<Y>());

        // for each future, add the value to the list
        for (Monad<X, Y> m : list) {
            For.of(mList, m, Fluent.ofBF(List::add));
        }

        // return the list, this is an unchecked cast but should only fail if there are two monads that extend Monad<X,?> where
        // X is the same.  That should never happen since X is supposed to be the Monad's class
        return (N) mList;
    }

    public static void main(String args[]) {
        List<OptionM<Integer>> lo = new ArrayList<>();
        lo.add(OptionM.sunit(1));
        lo.add(OptionM.sunit(2));
        lo.add(OptionM.sunit(3));

        OptionM<List<Integer>> omli = traverse(OptionM::sunit, lo);

        omli.map(Fluent.of(System.out::println));
    }
}
