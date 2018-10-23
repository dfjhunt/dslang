package dslang.temp.feh;

public class Ex2 {
    public <T> T ex2(MultExpr<T> e){
        return e.mult(e.num(2), e.num(3));
    }
}
