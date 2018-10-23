package dslang.temp;

import dslang.monad.Monad;
import dslang.monad.Writer;
import dslang.monad.Writer.t;
import dslang.monad.wrapper.OptionM;

public class ProductRepoWithLogging implements ProductRepository<Writer.t<String>> {

    @Override
    public Monad<Writer.t<String>, OptionM<Product>> findProduct(String id) {
        return new Writer<>("findProduct "+id+" = Joe",OptionM.of(new Product("Joe")));
    }

    @Override
    public Monad<Writer.t<String>, Boolean> saveProduct(Product product) {
        return new Writer<>("saveProduct "+product.getName(),true);
    }

    @Override
    public Monad<Writer.t<String>, Void> incrementProductSales(String id, int quantity) {
        return new Writer<>("incrementProductSales"+id, null);
    }

    @Override
    public <A> Monad<t<String>, A> create(A a) {
        return new Writer<>("", a);
    }
    
}
