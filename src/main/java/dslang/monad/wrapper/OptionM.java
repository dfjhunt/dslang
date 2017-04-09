package dslang.monad.wrapper;

import java.util.Optional;
import java.util.function.Function;

import dslang.functor.SplitFunctor;
import dslang.monad.Monad;
import dslang.monad.MonadWrapper;

public class OptionM<T> implements MonadWrapper<OptionM<?>, T, Optional<T>>, SplitFunctor<OptionM<?>, Void, T>{

    Optional<T> myOption = null;
    
    public OptionM(Optional<T> option) {
        myOption = option;
    }

    public static <U> OptionM<U> empty(){
        return new OptionM<U>(Optional.empty());
    }
    
    static public <S> OptionM<S> of(S s){
        return sunit(s);
    }
    
    @Override
    public <U> Monad<OptionM<?>, U> unit(U u) {
        return sunit(u);
    }

    public static <U> OptionM<U> sunit(U u){
        return new OptionM<U>(Optional.ofNullable(u));
    }
    
    static public <S> OptionM<S> wrap(Optional<S> option){
        return new OptionM<S>(option);
    }
    
    /*
     * All of the monad wrappers have an unwrap method to get the original Java "monad"
     */
    public Optional<T> unwrap() {
        return myOption;
    }
    
    static public <U> Optional<U> unwrap(Monad<OptionM<?>,U> t) {
        return ((OptionM<U>)t).unwrap();
    }
    
    @Override
    public <U> OptionM<U> map(Function<? super T, ? extends U> mapper) {
        return new OptionM<U>(myOption.map(mapper));
    }

    @Override
    public <U> Monad<OptionM<?>, U> flatMap(Function<? super T, ? extends Monad<OptionM<?>, U>> mapper) {
        Function<? super T, Optional<U>> newMapper = mapper.andThen(o -> ((OptionM<U>) o).unwrap());
        return new OptionM<U>(myOption.flatMap(newMapper));
    }

    public boolean isPresent(){
        return myOption.isPresent();
    }
    
    public T get(){
        return myOption.get();
    }
    
    public String toString(){
        return myOption.toString();
    }

    @Override
    public <C> SplitFunctor<OptionM<?>, Void, C> repair(Function<? super Void, ? extends C> left,
                                                          Function<? super T, ? extends C> right) {
        C c = isPresent()?right.apply(myOption.get()):left.apply(null);
        return OptionM.sunit(c);
    }
    
}
