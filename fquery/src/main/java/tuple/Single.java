package tuple;

public class Single<T> implements Tuple{
	
	T content;

	public Single(T content) {
		this.content = content;
	}

	public T get() {
		return content;
	}
	
	public <O>  More<O,Single<T>> add(O obj){
		return new More<O,Single<T>>(obj, this);
	}
	
	

}
