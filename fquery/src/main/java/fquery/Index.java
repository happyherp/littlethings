package fquery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;

import fquery.ChangingView.ChangeListener;

public class Index<T,A extends Comparable<A>> extends AbstractChangingView<Entry<A,Collection<T>>> {
	
	TreeMap<A, Collection<T>> index = new TreeMap<>();
	Function<T, A> accessor;
	ChangingView<T> source;
	
	public Index(ChangingView<T> source,  Function<T, A> accessor){
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
				A key = accessor.apply(obj);
				Collection<T> oldList = index.get(key);	
				ArrayList<T> newList = new ArrayList<>(oldList);
				newList.remove(obj);
				if (newList.isEmpty()){
					index.remove(key);
					fireRemove(new ImmutablePair<A, Collection<T>>(key,oldList));					
				}else{
					index.put(key, newList);
					fireChange(new ImmutablePair<A, Collection<T>>(key, oldList),
							new ImmutablePair<A, Collection<T>>(key, newList));					}
			}
		});
	}
	
	private void add(T obj){
		A key = this.accessor.apply(obj);
		if (!this.index.containsKey(key)){			
			this.index.put(key, new ArrayList<>());
			this.index.get(key).add(obj);			
			this.fireAdd(new ImmutablePair<A, Collection<T>>(key, this.index.get(key)));
		}else{
			Collection<T> oldList = this.index.get(key);
			ArrayList<T> newList = new ArrayList<>(oldList);
			newList.add(obj);
			this.index.put(key, newList);			
			this.fireChange(new ImmutablePair<A, Collection<T>>(key, oldList),
					new ImmutablePair<A, Collection<T>>(key, newList));			
		}
	}

	public Collection<T> get(A key){
		return index.get(key);
	}
	
	public List<T> sorted(){
		return index.values().stream()
				.flatMap(Collection::stream)
				.collect(Collectors.toList());
	}

	public Collection<T> lowerThan(A limit){
		return index.headMap(limit).values().stream()
				.flatMap(Collection::stream)
				.collect(Collectors.toList());
	}

	public Iterator<Entry<A,Collection<T>>> iterator() {
		return index.entrySet().iterator();
	}


	public List<T> top(int i) {
		return index.descendingMap().values().stream()
				.flatMap(Collection::stream)
				.limit(i)
				.collect(Collectors.toList());
	}


	
	

}
