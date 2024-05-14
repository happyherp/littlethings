package stateAlt;

public class Wrap<T> {
	
	private T content;

	public Wrap(T content){
		this.content = content;
	}
	
	public T get(){
		return this.content;
	}
	
	
	public static void main(String[] args){
		
		new Wrap<>("hi").get();
		
		
		new Wrap<>(new Wrap<>("hi")).get().get();
		
		
	}

}
