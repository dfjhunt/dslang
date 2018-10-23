
package dslang.temp.simple;

public class PrettyPrintExpr implements Expr<PrettyPrintWrapper<?>> {

    @Override
    public Wrap<PrettyPrintWrapper<?>, Integer> number(int n) {
        return new PrettyPrintWrapper<>("" + n);
    }

    @Override
    public Wrap<PrettyPrintWrapper<?>, Integer> add(
        Wrap<PrettyPrintWrapper<?>, Integer> n1,
        Wrap<PrettyPrintWrapper<?>, Integer> n2) {

        return new PrettyPrintWrapper<>("("+flip(n1).get() + "+" + flip(n2).get()+")");
    }

    @Override
    public Wrap<PrettyPrintWrapper<?>, Integer> increment(Wrap<PrettyPrintWrapper<?>, Integer> n) {
        return new PrettyPrintWrapper<>("(1+"+flip(n).get()+")");
    }
    
    private <A> PrettyPrintWrapper<A> flip(Wrap<PrettyPrintWrapper<?>, A> wrap){
        return (PrettyPrintWrapper<A>)wrap;
    }

}
