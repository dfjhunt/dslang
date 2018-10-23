package dslang.temp.feh;

public class Ex1 {
    public <T> T ex1(Expr<T> e){
        return e.add(e.num(2), e.num(3));
    }
}
