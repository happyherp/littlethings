package fquery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import fquery.ChangingList.ChangeListener;

public class Index<T,A extends Comparable<A>> {
	
	TreeMap<A, Collection<T>> index = new TreeMap<>();
	Function<T, A> accessor;
	ChangingList<T> source;
	
	public Index(ChangingList<T> source,  Function<T, A> accessor){
		this.source = source;
		this.accessor = accessor;
		
		source.forEach(this::add);
		
		source.addChangeListener(new ChangeListener<T>(){

			@Override
			public void onAdd(T obj) {
				Index.this.add(obj);				
			}

			@Override
			public void onRemove(T obj) {
				index.remove(accessor.apply(obj));
			}

			@Override
			public void onChange(T obj) {
				throw new RuntimeException("Not implemented");
				
			}
			
		});
		
	}
	
	
	private void add(T obj){
		A key = this.accessor.apply(obj);
		if (!this.index.containsKey(key)){
			this.index.put(key, new ArrayList<>());
		}
		this.index.get(key).add(obj);
	}
	


	public Collection<T> get(A key){
		return index.get(key);
	}
	
	public Collection<T> sorted(){
		return index.values().stream()
				.flatMap(Collection::stream)
				.collect(Collectors.toList());
	}

	public Collection<T> lowerThan(A limit){
		return index.headMap(limit).values().stream()
				.flatMap(Collection::stream)
				.collect(Collectors.toList());
	}

	public Iterator<Entry<A,Collection<T>>> iteratorKey() {
		return index.entrySet().iterator();
	}
	
	

}
