package fquery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Index<T,A extends Comparable<A>> {
	
	SortedMap<A, Collection<T>> index = new TreeMap<>();
	private Tokenizer<T> tokenizer;
	private Function<T, A> accessor;
	private Halde halde;
	
	public Index(Tokenizer<T> tokenizer, Function<T, A> accessor, Halde h){
		
		this.tokenizer = tokenizer;
		this.accessor = accessor;
		this.halde = h;
		
		h.plow(tokenizer).forEachRemaining(this::add);
		
		h.addContentListener(data ->{
			 tokenizer.tokenize(data)
			 	.forEach(this::add);
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



	public Iterator<Entry<A,Collection<T>>> iteratorKey() {
		return index.entrySet().iterator();
	}
	
	

}
