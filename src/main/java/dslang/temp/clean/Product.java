package dslang.temp.clean;

public class Product {
    
    String _name;
    
    public Product(String name){
        _name = name;
    }
    
    public Product setName(String name){
        return new Product(name);
    }
    
    public String getName(){
        return _name;
    }
    
    public String toString(){
        return "Product("+_name+")";
    }
}
