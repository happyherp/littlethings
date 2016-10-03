package fquery;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import com.google.common.collect.Lists;

public interface ChangingView<T> extends Iterable<T> {

	void addChangeListener(ChangeListener<T> l);
	
	public interface ChangeListener<T> {
			
		void onAdd(T obj);
		void onRemove(T obj);
		default void onChange(T oldO, T newO){
			this.onRemove(oldO);
			this.onAdd(newO);
		}
	}

	default Stream<T> stream(){
		return StreamUtils.asStream(this.iterator());
	}
	
	default List<T> asList(){
		return Lists.newArrayList(this);
	}
	
	default <A extends Comparable<A>> Index<T, A> index(Function<T, A> accessor){
		return new Index<T,A>(this, accessor);
	}
	
}
