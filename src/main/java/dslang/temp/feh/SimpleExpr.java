package dslang.temp.feh;

import dslang.temp.simple.IdWrapper;
import dslang.temp.simple.Wrap;

public class SimpleExpr implements ExprWBool<IdWrapper<?>> {

    @Override
    public Wrap<IdWrapper<?>, Integer> mult(Wrap<IdWrapper<?>, Integer> t1, Wrap<IdWrapper<?>, Integer> t2) {
        return new IdWrapper<>(flip(t1).get()*flip(t2).get());
    }

    @Override
    public Wrap<IdWrapper<?>, Integer> num(int i) {
        return new IdWrapper<>(i);
    }

    @Override
    public Wrap<IdWrapper<?>, Integer> add(Wrap<IdWrapper<?>, Integer> t1, Wrap<IdWrapper<?>, Integer> t2) {
        return new IdWrapper<>(flip(t1).get()+flip(t2).get());
    }

    @Override
    public Wrap<IdWrapper<?>, Boolean> bool(boolean t) {
        return new IdWrapper<>(t);
    }

    @Override
    public Wrap<IdWrapper<?>, Boolean> and(Wrap<IdWrapper<?>, Boolean> t1, Wrap<IdWrapper<?>, Boolean> t2) {
        return new IdWrapper<>(flip(t1).get()&&flip(t2).get());
    }

    
    private <A> IdWrapper<A> flip(Wrap<IdWrapper<?>, A> w){
        return (IdWrapper<A>)w;
    }
}
