
package dslang.monad.wrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dslang.monad.Monad;
import dslang.monad.MonadWrapper;

public class ListM<T> implements MonadWrapper<ListM<?>, T, List<T>>, Iterable<T> {

    List<T> myList = null;

    public ListM() {
        myList = new ArrayList<T>();
    }

    public ListM(List<T> list) {
        myList = list;
    }

    static public <S> ListM<S> of(List<S> list) {
        return new ListM<S>(list);
    }

    @Override
    public <U> ListM<U> unit(U u) {
        return sunit(u);
    }

    public static <U> ListM<U> sunit(U u) {
        return new ListM<U>(Arrays.asList(u));
    }

    public void add(T t) {
        myList.add(t);
    }

    @Override
    public List<T> unwrap() {
        return myList;
    }

    public static <S> List<S> unwrap(Monad<ListM<?>, S> m) {
        return ((ListM<S>) m).unwrap();
    }

    @Override
    public <U> ListM<U> map(Function<? super T, ? extends U> mapper) {
        return new ListM<U>(myList.stream().map(mapper).collect(Collectors.toList()));
    }

    @Override
    public <U> ListM<U> flatMap(Function<? super T, ? extends Monad<ListM<?>, U>> mapper) {
        Function<? super T, Stream<U>> newMapper = mapper.andThen(o -> ((ListM<U>) o).unwrap().stream());

        return new ListM<U>(myList.stream().flatMap(newMapper).collect(Collectors.toList()));
    }

    public Iterator<T> iterator() {
        return myList.iterator();
    }

    public String toString() {
        return myList.toString();
    }
}
