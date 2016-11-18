package dslang.monad.transformer;

import java.util.function.Function;

import dslang.monad.Monad;
import dslang.monad.MonadT;
import dslang.monad.Try;
import dslang.monad.wrapper.FutureM;
import dslang.monad.wrapper.OptionM;
import dslang.util.function.Fluent;


public class TryT<M, T> implements MonadT<TryT<M, ?>, M, T, Try<?>>{

    Monad<M, Try<T>> myMonad = null;

    public TryT(Monad<M, Try<T>> monad) {
        myMonad = monad;
    }

    public static <N, S> TryT<N, S> of(Monad<N, S> m){
        return new TryT<N,S>(m.map(t->Try.of(t)));
    }
    
    @Override
    public <U> TryT<M,U> unit(U u) {
        return new TryT<M, U>(myMonad.unit(Try.of(u)));
    }
    
    public Monad<M, Try<T>> run() {
        return myMonad;
    }
    
    public TryT<M, T> getM(){
        return this;
    }

    
    public Monad<M, Monad<Try<?>, T>> lift(){
        return myMonad.map(x->(Monad<Try<?>, T>)x);
    }
    
    public M liftM(){
        return myMonad.getM();
    }
    
    @SuppressWarnings("unchecked")
    public <U,X> Monad<M,U> lift(Function<? super Try<X>, ? extends U> mapper) {
        Function<Try<T>, Try<X>> uncheckedFix = t->(Try<X>)t; 
        return myMonad.map(mapper.compose(uncheckedFix));
    }

    //TODO - catch exceptions
    @Override
    public <U> TryT<M, U> map(Function<? super T, ? extends U> mapper) {
        Monad<M, Try<U>> t = myMonad.map(o -> o.map(mapper));
        return new TryT<M, U>(t);
    } 
    
    private <U> Monad<M, Try<U>> mapTryToM(Function<? super T, Monad<M, Try<U>>> mapper, Try<T> t) {
        Function<Exception, Monad<M,Try<U>>> forException = e->myMonad.unit(Try.exception(e));
        
        if (!t.isException()) {
            try {
                return mapper.apply(t.get());
            } catch (Exception e) {
                return forException.apply(e);
            }
        } else {
            return forException.apply(t.getException());
        }
    }
    
    @Override
    public <U> TryT<M, U> flatMap(Function<? super T, ? extends Monad<TryT<M, ?>, U>> mapper) {
        Function<? super T, Monad<M, Try<U>>> interimMapper = x -> {
            return ((TryT<M, U>) mapper.apply(x)).run();
        };
        
        Function<Try<T>, Monad<M, Try<U>>> newMapper = t -> mapTryToM(interimMapper, t);
        
        return new TryT<M, U>(myMonad.flatMap(newMapper));
    }

    public static <X> OptionT<TryT<FutureM<?>, ?>, X> help(X val) {
        return  OptionT.of(TryT.of(FutureM.sunit(val)));
    }

    public static void main(String args[]) {
        // Future[Try[Option[Integer]]]
        OptionT<TryT<FutureM<?>, ?>, Integer> temp = help(3);
        System.out.println("created");
        temp = temp.map(x -> x + 1);
        
        Function<Object, Object> println = Fluent.of(System.out::println);
        
        temp.map(println);

        OptionT<TryT<FutureM<?>, ?>, String> tempx = temp.map(x->""+x);
        
        final OptionT<TryT<FutureM<?>, ?>, Integer> temp1 = help(5);
        final OptionT<TryT<FutureM<?>, ?>, Integer> temp2 = help(7);

        OptionT<TryT<FutureM<?>, ?>, Integer> temp3 = temp1.flatMap(x -> temp2);
        
        temp3.map(println);
        
        
        //test mapping the value to a null
        System.out.println("test null");
        OptionT<TryT<FutureM<?>, ?>, Integer> nullVal = temp3.map(x->null);   
        nullVal.map(x->x+3);
        
        System.out.print("val: *");
        nullVal.map(println);
        
        System.out.print("*\nisPresent: ");
        nullVal.lift(OptionM::isPresent).map(println);
        
        
        Monad<TryT<FutureM<?>,?>,Boolean> present = nullVal.lift(OptionM::isPresent);
        
        temp3.map(x->null).lift(OptionM::isPresent).map(println);
        temp3.lift(OptionM::isPresent).map(println);
        
        System.out.println("\n\ntest exception");
        OptionT<TryT<FutureM<?>, ?>, Integer> exVal = temp3.map(x->{throw new RuntimeException();});
        exVal.map(x->x+3);
        
        System.out.print("val: *");
        exVal.map(println);
        
        System.out.print("*\nisException: ");
        exVal.liftM().lift(Try::isException).map(println);
        
        System.out.print("Inner Optional value: ");
        exVal.lift().map(println);
        
        
        FutureM<Integer> fm = (FutureM<Integer>)temp3.lift(OptionM::get).getM().lift((Try<Integer> x)->{
            try{
                return x.get();
            }catch(Exception e){
                return null;
            }
        });
        //System.out.println("\n"+fm.unwrap().get());
        
        
        TryT<FutureM<?>, Boolean> a = (TryT<FutureM<?>, Boolean>)temp3.run().map(OptionM::isPresent);
        Function<Try<Integer>, Integer> f = x->1;
        Monad<FutureM<?>, Integer> t = temp3.lift().getM().lift(f);
    }
}
