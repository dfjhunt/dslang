
package dslang.temp;

import java.util.function.Function;

import dslang.monad.For;
import dslang.monad.Monad;
import dslang.monad.wrapper.OptionM;

public class Program<M> {
    ProductRepository<M> repo;

    public Program(ProductRepository<M> pr) {
        this.repo = pr;
    }

    public Monad<M, Boolean> renameProduct(String id, String name) {
        return For.of( //
            repo.saveProduct(new Product(id)), //
            __ -> repo.findProduct(id), //
            p1 -> {
                OptionM<Monad<M, Boolean>> opt = p1.map(x -> x.setName(name)).map(repo::saveProduct);
                return opt.unwrap().orElse(repo.create(false));
            });
    }

    public Monad<M, OptionM<Product>> updateProduct(String id, Function<Product, Product> f) {
        return repo.findProduct(id).map(p -> {
            final OptionM<Product> opt = p.map(f);
            return opt.map(repo::saveProduct).flatMap(__ -> opt);
        });
    }
    
    public Monad<M, OptionM<Product>> incrementCount(String id, int delta){
        return updateProduct(id, p->new Product(id, p.get_count()+delta));
    }
    
    
}
