
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
import dslang.util.Utility;

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
    
    @SafeVarargs
	static public <S> ListM<S> of(S... s) {
        return new ListM<S>(Arrays.asList(s));
    }

    @Override
    public <U> ListM<U> unit(U u) {
        return sunit(u);
    }

    @SafeVarargs
	public static <U> ListM<U> sunit(U... u) {
    	//TODO: the list returned by this doesn't support add
        return new ListM<U>(Arrays.asList(u));
    }

    public boolean add(T t) {
        return myList.add(t);
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

    @SuppressWarnings("unchecked")
    public <M extends Monad<X, U>, N extends Monad<X, ListM<U>>, U, X> N traverse(Function<ListM<U>, N> unit,
			Function<T, M> map) {
        Monad<X, List<U>> ml =  unit.apply(new ListM<>()).map(x->x.unwrap());
        Function<List<U>, ListM<U>> wrap = ListM::of;
        
        //Casting Monad<X,ListM<U>> to N should be ok as long as there is only one class that extends Monad<X,T>
		return (N)Utility.traverseGen(ml, map, (Iterable<T>)this).map(wrap);
	}
}
