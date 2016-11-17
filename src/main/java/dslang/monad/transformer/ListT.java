package dslang.monad.transformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import dslang.monad.Monad;
import dslang.monad.MonadT;
import dslang.monad.wrapper.OptionM;


public class ListT<M, T> implements MonadT<ListT<M, ?>, T> {

    Monad<M, List<T>> myMonad = null;

    public ListT(Monad<M, List<T>> monad) {
        myMonad = monad;
    }

    public Monad<M, List<T>> run() {
        return myMonad;
    }
    
    public ListT<M, T> getM(){
        return this;
    }
    
    public M lift(){
        return myMonad.getM();
    }
    
    @SuppressWarnings("unchecked")
    public <U,X> Monad<M,U> lift(Function<? super List<X>, ? extends U> mapper) {
        Function<List<T>, List<X>> uncheckedFix = t->(List<X>)t; 
        return myMonad.map(mapper.compose(uncheckedFix));
    }
    
    public static <N, S> ListT<N, S> of(Monad<N, S> m) {
        return new ListT<N, S>(m.map(t -> Arrays.asList(t)));
    }

    @Override
    public <U> Monad<ListT<M, ?>, U> unit(U u) {
        return new ListT<M, U>(myMonad.unit(Arrays.asList(u)));
    }

    @Override
    public <U> ListT<M, U> map(Function<? super T, ? extends U> mapper) {
        Monad<M, List<U>> m = myMonad.map(l -> {
            List<U> nl = new ArrayList<U>();
            for(T t: l){
                nl.add(mapper.apply(t));
            }
            return nl;
        });
        return new ListT<M, U>(m);
    }
    
    private <U> Monad<M, List<U>> combine(List<Monad<M, List<U>>> l){
        Monad<M, List<U>> temp = myMonad.unit(new ArrayList<U>());
        for(Monad<M, List<U>> m : l){
            temp = temp.flatMap(lu->m.map(mu->{lu.addAll(mu); return lu;}));
        }
        return temp;
    }
    
    @Override
    public <U> ListT<M,U> flatMap(Function<? super T, ? extends Monad<ListT<M, ?>, U>> mapper) {
        Function<? super T, Monad<M, List<U>>> newMapper = x -> {
            return ((ListT<M, U>) mapper.apply(x)).run();
        };

        Monad<M, List<U>> m = myMonad.flatMap((List<T> l) -> {
            List<Monad<M, List<U>>> nl = new ArrayList<Monad<M, List<U>>>();
            for (T t : l){
                nl.add(newMapper.apply(t));
            }
            return combine(nl);
        });

        return new ListT<M, U>(m);
    }

    public static void main(String args[]) {
       
        ListT<OptionM<?>, Integer> listT = new ListT<OptionM<?>, Integer>(OptionM.sunit(Arrays.asList(1, 2, 3)));
       
       Function<Integer, List<Integer>> f = x->Arrays.asList(x, 2*x);
       
       Function<Integer, ListT<OptionM<?>, Integer>> f2 = f.andThen(OptionM::sunit).andThen(ListT::new);
       
       List<Integer> l = OptionM.unwrap(listT.flatMap(f2).run()).get();
       
       l.forEach(System.out::println);
       
    }
}
