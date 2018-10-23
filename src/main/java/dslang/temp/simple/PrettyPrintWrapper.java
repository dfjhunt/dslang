package dslang.temp.simple;

public class PrettyPrintWrapper<A> implements Wrap<PrettyPrintWrapper<?>, A>{
    String _str;
    public PrettyPrintWrapper(String str){
        _str = str;
    }
    
    public PrettyPrintWrapper<A> concat(String str){
        return new PrettyPrintWrapper<>(_str.concat(str));
    }
    
    public String get(){
        return _str;
    }
}
