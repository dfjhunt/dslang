
package dslang.temp.clean;

public class Program {
    ProductRepository repo;

    public Program(ProductRepository pr) {
        this.repo = pr;
    }

    public M<Product> renameProduct(String id, String name) {
        return repo.saveProduct(new Product(id))//
            .flatMap(__ -> repo.findProduct(id))//
            .flatMap(p1 -> {
                Product temp = p1.setName(name);
                return repo.saveProduct(temp).map(b -> temp);
            });
    }
}
