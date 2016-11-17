package dslang.monad.wrapper;

import java.util.Optional;
import java.util.function.Function;

import dslang.monad.Monad;

public class OptionM<T> implements Monad<OptionM<?>, T> {

    Optional<T> myOption = null;

    public Optional<T> unwrap() {
        return myOption;
    }

    public String toString(){
        return myOption.toString();
    }
    
    static public <U> Optional<U> unwrap(Monad<OptionM<?>,U> t) {
        return ((OptionM<U>)t).unwrap();
    }
    
    public OptionM(Optional<T> option) {
        myOption = option;
    }

    public static <U> OptionM<U> empty(){
        return new OptionM<U>(Optional.empty());
    }
    
    static public <S> OptionM<S> of(Optional<S> option){
        return new OptionM<S>(option);
    }
    
    @Override
    public <U> Monad<OptionM<?>, U> unit(U u) {
        return new OptionM<U>(Optional.of(u));
    }

    public static <U> OptionM<U> sunit(U u){
        return new OptionM<U>(Optional.of(u));
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
    
}
