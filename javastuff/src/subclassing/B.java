package subclassing;

import java.util.List;

public class B extends A {
    
    @Override
    public B get(){
	return this;
    }
    
    public List<B> getList(){
	return null;
    }

}
