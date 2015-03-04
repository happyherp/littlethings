package proxy;

import java.util.HashMap;
import java.util.Map;

public class FunctionCache implements Function {
    
    private Function f;
    
    Map<Integer, Integer> values;

    public FunctionCache(Function f){
	this.f = f;
	this.values = new HashMap<>();
    }

    @Override
    public int at(int x) {
	
	int y;
	if (this.values.containsKey(x)){
	    y =  this.values.get(x);
	}else{
	    y = f.at(x);
	    this.values.put(x, y);
	}
	return y;	
    }
    
    
    
    static public void main(String[] args){
	
	Function f = new FunctionCache(new Factorial());
	System.out.println(f.at(5));
	System.out.println(f.at(5));
	System.out.println(f.at(6));
	
    }

}
