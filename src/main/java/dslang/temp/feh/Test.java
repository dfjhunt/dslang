package dslang.temp.feh;

import dslang.temp.simple.IdWrapper;

public class Test {
    public static void main(String args[]){
        IdWrapper<Integer> temp = (IdWrapper<Integer>)(new Ex3()).ex3_1(new SimpleExpr());
        System.out.println(temp.get());
        
        IdWrapper<Boolean> temp2 = (IdWrapper<Boolean>)(new Ex3()).ex3_2(new SimpleExpr());
        System.out.println(temp2.get());
    }
}
