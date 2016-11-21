package dslang.monad;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import dslang.util.function.CheckedFunction;
import dslang.util.function.CheckedSupplier;

public class Try<T> implements Monad<Try<?>, T>{

    private final Exception e;

    private final T value;

    public Try(T value) {
        this.value = value;
        this.e=null;
    }
    
    public Try(Exception e) {
      this.e = e;
      this.value = null;
    }

    public static <T> Try<T> exception(Exception e) {
        return new Try<>(e);
    }

    public Exception getException(){
        return e;
    }
    
    public static <T> Try<T> of(T value) {
        return new Try<T>(value);
    }
    
    public static <T> Try<T> of(Supplier<T> thunk) {
        try{
          return of(thunk.get());
        }catch(Exception e){
          return exception(e);
        }
    }

    public static <T,E extends Exception> Try<T> ofChecked(CheckedSupplier<T,E> thunk) {
        try{
          return of(thunk.get());
        }catch(Exception e){
          return exception(e);
        }
    }

    @Override
    public <U> Monad<Try<?>, U> unit(U u) {
        return of(u);
    }

    @Override
    public Try<?> getM() {
        return this;
    }
    
    public static <A,B,E extends Exception> Function<A, Try<B>> check(CheckedFunction<A,B,E> f){
        return x->Try.ofChecked((CheckedSupplier<B, E>)()->f.apply(x));
    }
    
    public T get() throws Exception{
        if (value == null) {
            throw e;
        }
        return value;
    }

    public boolean isException(){
      return e!=null;
    }
    
    //TODO - find common code with TryT for catching exceptions
    public <U> Try<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        if (isException())
            return exception(e);
        else {
            try {
                return new Try<U>(mapper.apply(value));
            }catch(Exception ex) {
                return exception(ex);
            }
        }
    }
    
    @Override
    public <U> Try<U> flatMap(Function<? super T, ? extends Monad<Try<?>, U>> mapper) {
        Objects.requireNonNull(mapper);
        if (isException())
            return  exception(e);
        else {
            return (Try<U>)mapper.apply(value);
        }
    }
    
    public T orElse(T other) {
        return !isException() ? value : other;
    }

    public T orElseGet(Supplier<? extends T> other) {
        return !isException() ? value : other.get();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Try)) {
            return false;
        }

        Try<?> other = (Try<?>) obj;
        return Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return e == null ? String.format("Try[%s]", value)
                : "Try.exception";
    }
    
    public static void main(String args[]){
        CheckedFunction<Integer, Integer, Exception> f = x->{throw new Exception("Test");};
        
        Function<Integer, Try<Integer>> fChecked = Try.check(f);
        
        Try<Integer> t = fChecked.apply(3);
        
        
    }

}