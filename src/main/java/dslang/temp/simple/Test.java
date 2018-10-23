
package dslang.temp.simple;

public class Test {
    public static void main(String args[]) {
        Program<IdWrapper<?>> temp = new Program<>(new IdExpr());
        System.out.println(((IdWrapper<?>)temp.calculate()).get());
        
        Program<PrettyPrintWrapper<?>> temp2 = new Program<>(new PrettyPrintExpr());
        System.out.println(((PrettyPrintWrapper<?>)temp2.calculate()).get());
    }
}
