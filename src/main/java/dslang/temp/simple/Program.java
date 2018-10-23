package dslang.temp.simple;

public class Program<T> {
    Expr<T> _expr;
    
    public Program(Expr<T> expr){
        _expr = expr;
    }
    
    public Wrap<T, Integer> calculate(){
        return _expr.add(_expr.increment(_expr.number(1)), _expr.number(3));
    }
}
