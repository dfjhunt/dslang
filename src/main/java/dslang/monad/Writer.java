
package dslang.monad;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import dslang.Monoid;
import dslang.util.Pair;

public class Writer<W, A> implements Monad<Writer.t<W>, A> {
    public static class t<W>{
    }
    
    List<W> _log = Arrays.asList();

    A _a;

    public Writer(A a) {
        _a = a;
    }

    public Writer(W w, A a) {
        _a = a;
        _log = Arrays.asList(w);
    }
    
    public Writer(List<W> log, A a) {
        _a = a;
        _log = log;
    }

    public A get(){
        return _a;
    }
    
    public Pair<A, W> runWriter(Monoid<W> m){
        return Pair.of(_a, m.collapse(_log));
    }
    
    public List<W> getLog() {
        return _log;
    }
    
    public void setLog(List<W> log){
        _log = log;
    }

    @Override
    public <U> Monad<Writer.t<W>, U> unit(U u) {
        return new Writer<>(u);
    }

    @Override
    public <U> Writer<W, U> flatMap(Function<? super A, ? extends Monad<Writer.t<W>, U>> mapper) {
        Writer<W, U> writer = (Writer<W, U>) mapper.apply(_a);
        List<W> temp = new ArrayList<>();
        temp.addAll(_log);
        temp.addAll(writer.getLog());
        writer.setLog(temp);
        return writer;
    }

}
