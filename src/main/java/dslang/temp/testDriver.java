
package dslang.temp;

import java.util.HashMap;
import java.util.Map;

import dslang.monad.Reader;
import dslang.monad.Writer;
import dslang.monad.transformer.OptionT;
import dslang.monad.wrapper.FutureM;
import dslang.monad.wrapper.OptionM;

public class testDriver {
    public static void main(String args[]) {

        Program<FutureM.t> program = new Program<>(new ProductRepoWithFuture());
        FutureM<Boolean> result = (FutureM<Boolean>) program.renameProduct("Joe", "Foo");
        try {
            System.out.println(result.get());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Program<Writer.t<String>> program2 = new Program<>(new ProductRepoWithLogging());
        Writer<String, Boolean> result2 = (Writer<String, Boolean>) program2.renameProduct("Joe", "Foo");
        try {
            System.out.println(result.get());
            System.out.println("***LOG****");
            System.out.println(result2.getLog());
            System.out.println("**********");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Program<Writer.t<String>> program3 = new Program<>(new ProductRepoWithKVS<>(new WriterKVS()));
        Writer<String, Boolean> result3 = (Writer<String, Boolean>) program3.renameProduct("Joe", "Foo");
        try {
            System.out.println(result3.get());
            System.out.println("***LOG****");
            System.out.println(result3.getLog());
            System.out.println("**********");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Program<OptionT<FutureM.t, ?>> program4 = new Program<>(new ProductRepoWithKVS<>(new ComplexKVS()));
        OptionT<FutureM.t, Boolean> result4 = (OptionT<FutureM.t, Boolean>) program4.renameProduct("Joe", "Foo");
        try {
            FutureM<OptionM<Boolean>> temp = result4.run();
            System.out.println(temp.get().get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        Program<Reader.t<Map<Object, Object>>> program5 = new Program<>(new ProductRepoWithKVS<>(new EmptyKVS()));
        Reader<Map<Object, Object>, Boolean> result5 =  Reader.wrap(program5.renameProduct("Joe", "Foo"));
        try {
            System.out.println(result5.run(new HashMap<>()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
