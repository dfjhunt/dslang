package dslang.temp;

public class Product {
    
    String _name;
    int _count=0;
    
    public Product(String name){
        _name = name;
    }
    
    public Product(String name, int count){
        _name = name;
        _count = count;
    }
    
    public int get_count() {
        return _count;
    }

    public void set_count(int _count) {
        this._count = _count;
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
