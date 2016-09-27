package fquery;

import java.util.stream.Stream;

public interface ChangingList<T> extends Iterable<T> {

	void addChangeListener(ChangeListener<T> l);
	
	public interface ChangeListener<T> {
			
		void onAdd(T obj);
		void onRemove(T obj);
		void onChange(T obj);
	}

	default Stream<T> stream(){
		return StreamUtils.asStream(this.iterator());
	}
	
}
