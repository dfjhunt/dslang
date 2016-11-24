package dslang.monad.transformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import dslang.monad.Monad;
import dslang.monad.MonadT;
import dslang.monad.wrapper.ListM;


public class ListT<M, T> implements MonadT<ListT<M, ?>, M, T, ListM<?>> {

    Monad<M, ListM<T>> myMonad = null;

    
    public ListT(Monad<M, ListM<T>> monad) {    
        myMonad = monad;
    }

    public static <N, S> ListT<N, S> of(Monad<N, S> m) {
        return new ListT<N, S>(m.map(t -> ListM.of(Arrays.asList(t))));
    }

    @Override
    public <U> Monad<ListT<M, ?>, U> unit(U u) {
        return new ListT<M, U>(myMonad.unit(ListM.of(Arrays.asList(u))));
    }
    
    public Monad<M, ListM<T>> run() {
        return myMonad;
    }
    
    public Monad<M, Monad<ListM<?>, T>> lift(){
        return myMonad.map(x->(Monad<ListM<?>, T>)x);
    }
    
    @SuppressWarnings("unchecked")
    public <U,X> Monad<M,U> lift(Function<? super ListM<X>, ? extends U> mapper) {
        Function<ListM<T>, ListM<X>> uncheckedFix = t->(ListM<X>)t; 
        return myMonad.map(mapper.compose(uncheckedFix));
    }

    @Override
    public <U> ListT<M, U> map(Function<? super T, ? extends U> mapper) {
        Monad<M, ListM<U>> m = myMonad.map(l -> {
            List<U> nl = new ArrayList<U>();
            for(T t: l.unwrap()){
                nl.add(mapper.apply(t));
            }
            return ListM.of(nl);
        });
        return new ListT<M, U>(m);
    }
    
    private <U> Monad<M, ListM<U>> flatten(ListM<Monad<M, ListM<U>>> l){
        Monad<M, ListM<U>> temp = myMonad.unit(ListM.of(new ArrayList<U>()));
        for(Monad<M, ListM<U>> m : l.unwrap()){
            temp = temp.flatMap(lu->m.map(mu->{lu.unwrap().addAll(mu.unwrap()); return lu;}));
        }
        return temp;
    }
    
    @Override
    public <U> ListT<M,U> flatMap(Function<? super T, ? extends Monad<ListT<M, ?>, U>> mapper) {
        Function<? super T, Monad<M, ListM<U>>> newMapper = x -> {
            return ((ListT<M, U>) mapper.apply(x)).run();
        };

        Monad<M, ListM<U>> m = myMonad.flatMap((ListM<T> l) -> {
            ListM<Monad<M, ListM<U>>> nl = ListM.of(new ArrayList<Monad<M, ListM<U>>>());
            for (T t : l.unwrap()){
                nl.unwrap().add(newMapper.apply(t));
            }
            return flatten(nl);
        });

        return new ListT<M, U>(m);
    }
}
