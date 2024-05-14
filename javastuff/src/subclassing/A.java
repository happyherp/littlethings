package subclassing;

import java.util.List;

public class A {
    
    public A get(){
	return this;
    }
    
    public List<? extends A> getList(){
	return null;
    }

}
