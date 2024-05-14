package proxy;

public class Factorial implements Function {

    @Override
    public int at(int x) {
	
	int y = 1;
	while (x>0){
	    y*= x;
	    x--;
	}
	
	return y;
    }

}
