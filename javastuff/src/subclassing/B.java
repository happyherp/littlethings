package subclassing;

public class B extends A {
    
    @Override
    public B get(){
	return this;
    }

}
