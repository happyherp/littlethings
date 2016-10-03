package tuple;

public class More<T, O extends Tuple> implements Tuple{
	
	T content;
	O other;


	public T get() {
		return content;
	}
	public O next(){
		return other;
	}
	public More(T content, O other) {
		super();
		this.content = content;
		this.other = other;
	}
	public <N> More<N, More<T, O>> add(N l) {
		return new More<N, More<T,O>>(l, this);
	}
	
}
