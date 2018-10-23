package dslang.temp.simple;

public class IdExpr implements Expr<IdWrapper<?>>{

    @Override
    public Wrap<IdWrapper<?>, Integer> number(int n) {
        return new IdWrapper<>(n);
    }

    @Override
    public Wrap<IdWrapper<?>, Integer> add(Wrap<IdWrapper<?>, Integer> n1, Wrap<IdWrapper<?>, Integer> n2) {
        return new IdWrapper<>(((IdWrapper<Integer>)n1).get()+((IdWrapper<Integer>)n2).get());
    }

    @Override
    public Wrap<IdWrapper<?>, Integer> increment(Wrap<IdWrapper<?>, Integer> n) {
        return new IdWrapper<>(((IdWrapper<Integer>)n).get()+1);
    }

}
