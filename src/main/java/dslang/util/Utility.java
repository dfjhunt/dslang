
package dslang.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import dslang.monad.Monad;
import dslang.monad.Try;
import dslang.monad.wrapper.FutureM;
import dslang.monad.wrapper.ListM;
import dslang.monad.wrapper.OptionM;
import dslang.util.function.Fluent;

public class Utility {
    public static <M extends Monad<X, Y>, N extends Monad<X, List<Y>>, X, Y, Z> N traverse(Function<List<Y>, N> unit,
                                                                                           Function<Z, M> map, List<Z> listz) {
        return traverseGen(unit.apply(new ArrayList<Y>()), map, listz);
    }

    public static <M extends Monad<X, Y>, N extends Monad<X, List<Y>>, X, Y> N sequence(Function<List<Y>, N> unit,
                                                                                        List<M> list) {
        return traverseGen(unit.apply(new ArrayList<Y>()), x -> x, list);
    }

    public static <M extends Monad<X, Y>, N extends Monad<X, ListM<Y>>, X, Y> N sequenceListM(Function<ListM<Y>, N> unit, ListM<M> list) {
        Function<List<Y>, Monad<X, List<Y>>> lUnit = l -> unit.apply(ListM.of(l)).map(x -> x.unwrap());
        Function<List<Y>, ListM<Y>> wrap = ListM::of;
        return (N) sequence(lUnit, list.unwrap()).map(wrap);
    }

    @SuppressWarnings("unchecked")
    public static <M extends Monad<X, Y>, N extends Monad<X, A>, X, Y, Z, A extends List<Y>> N traverseGen(N mList,
                                                                                                           Function<Z, M> map,
                                                                                                           Iterable<Z> listz) {

        List<M> list = StreamUtil.fromIterator(listz.iterator()).map(map).collect(Collectors.toList());

        BiFunction<A, Y, A> add = (a, y) -> {
            a.add(y);
            return a;
        };

        BiFunction<A, Monad<X, Y>, N> f = (a, m) -> (N) m.map(y -> {
            return add.apply(a, y);
        });

        // for each monad, add the value to the list
        for (Monad<X, Y> m : list) {
            mList = (N) mList.flatMap(a -> f.apply(a, m));
        }

        // return the list, this is an unchecked cast but should only fail if
        // there are two monads that extend Monad<X,?> where X is the same. That
        // should never happen since X is supposed to be the Monad's class
        return (N) mList;
    }

    public static <X> ListM<X> flatten(ListM<ListM<X>> llx) {
        return llx.flatMap(x -> x);
    }

    public static void main(String args[]) {
        Function<ListM<String>, OptionM<ListM<String>>> unit = OptionM::sunit;

        OptionM<ListM<String>> t = ListM.of(Arrays.asList(1, 2, 3)).traverse(unit, i -> OptionM.of("" + i));

        System.out.println(t);

        CompletableFuture<Integer> cf = new CompletableFuture<>();
        cf.completeExceptionally(new NullPointerException());

        List<FutureM<Integer>> lfi = Arrays.asList(FutureM.sunit(3), FutureM.of(cf));

        Function<FutureM<Integer>, FutureM<Try<Integer>>> safe = f -> FutureM.of(f.unwrap().handle((i, thr) -> {
            if (thr != null) {
                return Try.exception((Exception) thr);
            } else {
                return Try.of(i);
            }
        }));

        FutureM<List<Try<Integer>>> fut = traverse(FutureM::sunit, safe, lfi);
        fut.map(Fluent.of(System.out::println));

    }

    public <S extends T, T> Function<T, Optional<S>> castOpt(Class<S> cls) {
        return t -> {
            if (!cls.isInstance(t))
                return Optional.empty();
            else
                return Optional.of(cls.cast(t));
        };
    }
}
