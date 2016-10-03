package fquery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;

import fquery.bank.AccountBalance;

/**
 * Join over any number of Sources.
 * 
  *
 */
public class MultiJoin <K extends Comparable<K>, T, L> extends AbstractChangingView<T> {
	
	Map<K, T> cache;
	private List<? extends Index<L, K>> sources;
	private BiFunction<K,List<Collection<L>>,T> resultcombiner;
	
	
	public MultiJoin(List<? extends Index<L, K>> sources,
			BiFunction<K,List<Collection<L>>,T> resultcombiner){
		
		this.sources = sources;
		this.resultcombiner = resultcombiner;
	
	
		cache =  joinAll(sources, resultcombiner);
		
		sources.forEach(source->source.source.addChangeListener(new ChangeListener<L>() {

			@Override
			public void onAdd(L obj) {
				K key = source.accessor.apply(obj);
				MultiJoin.this.recalculateKey(key);
			}

			@Override
			public void onRemove(L obj) {
				this.onAdd(obj);			
			}
		}));
	}


	protected void recalculateKey(K key) {
		if (this.cache.containsKey(key)){
			this.fireRemove(this.cache.get(key));
		}
		
		
		T result = this.resultcombiner.apply(key,  
			this.sources.stream()
				.map(s-> s.get(key) != null? s.get(key): (Collection<L>) Collections.emptyList())
				.collect(Collectors.toList()));

		this.cache.put(key,result);
		this.fireAdd(result);			
	}


	@Override
	public Iterator<T> iterator() {
		
		return cache.values().iterator();
	}

	
	
	public static <R,K extends Comparable<K>,L> Map<K,R>  joinAll(
			List<? extends Index<L, K>> sources2,
			BiFunction<K, List<Collection<L>>, R> resultcombiner2) {
		
		
		Map<K, R> joined = new HashMap<>();
			
		List<PeekingIterator<Entry<K,Collection<L>>>> iterators = sources2.stream()
				.map(i -> Iterators.peekingIterator(i.iterator()))
				.filter(Iterator::hasNext)
				.collect(Collectors.toList());
						
		
		while (iterators.stream().anyMatch(Iterator::hasNext)){
			K smallestKey = iterators.stream()
					.filter(Iterator::hasNext)
					.map(s -> s.peek().getKey())
					.collect(Collectors.minBy(Comparator.naturalOrder())).get();
			
			 List<Collection<L>> nextRow = iterators.stream()
				.map(i->{
					if (i.hasNext() &&  i.peek().getKey().equals(smallestKey)){
						return i.next().getValue();
					}else{
						return (Collection<L>) Collections.emptyList();
					}
				})
				.collect(Collectors.toList());

			R result = resultcombiner2.apply(smallestKey, nextRow);
			joined.put(smallestKey, result);	
		}
		return joined;
	}
	
	
	public static  <T> T nextOrNull (Iterator<T> iter){
		return iter.hasNext() ? iter.next():null;
	}
	
	@FunctionalInterface
	public interface JoinFunction<T,K,L,R>{
		
		public T apply(K key, Collection<L> left, Collection<R> right );
		
	}

	public T getKey(K key) {
		return cache.get(key);
	}

}
