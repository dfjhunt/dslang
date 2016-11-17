package dslang.monad.wrapper;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dslang.monad.Monad;

public class ListM<T> implements Monad<ListM<?>, T> {

    List<T> myList = null;

    public List<T> unwrap() {
        return myList;
    }
    
    public static <S> List<S> unwrap(Monad<ListM<?>,S> m) {
        return ((ListM<S>)m).unwrap();
    }

    public ListM(List<T> list) {
        myList = list;
    }

    static public <S> ListM<S> of(List<S> list) {
        return new ListM<S>(list);
    }

    @Override
    public <U> ListM<U> unit(U u) {
        return new ListM<U>(Arrays.asList(u));
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
}
