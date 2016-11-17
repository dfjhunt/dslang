package dslang.monad;

import java.util.Optional;

import dslang.monad.wrapper.OptionM;
import dslang.util.Pair;

public class TupleTest {
    public static <M, U, T> Monad<M, Pair<U,T>>  tuple(Monad<M, U> one, Monad<M, T> two){
        return one.flatMap(o->{
            return two.map(t->new Pair<U,T>(o,t));
        });
    }
    
    public static Optional<String> getUsername(){
        return Optional.of("Dan");
    }
    
    public static Optional<String> getId(){
        return Optional.empty();
    }
    
    public static Optional<String> getUserKey(){
        return getUsername().flatMap(f->getId().map(l->f.concat(l)));
    }
    
    public static void main(String args[]){
        Monad<OptionM<?>, Integer> one = OptionM.of(Optional.of(1));
        Monad<OptionM<?>, Integer> two = OptionM.of(Optional.of(2));
        
        Monad<OptionM<?>, Pair<Integer, Integer>> pair = TupleTest.tuple(one, two);
        
       // System.out.println(pair.getM().unwrap().get());
        System.out.println(TupleTest.getUserKey());
    }
}
