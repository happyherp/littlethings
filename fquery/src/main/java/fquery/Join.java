package fquery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiFunction;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;

public class Join<K extends Comparable<K>, T, L,R> extends AbstractChangingList<T> {
	
	Map<K, T> cache;
	private Index<L, K> leftSource;
	private Index<R, K> rightSource;
	private BiFunction<Collection<L>, Collection<R>, T> resultcombiner;
	
	public Join(Index<L, K> leftSource, 
						Index<R, K> rightsource, 
					   BiFunction<Collection<L>, Collection<R>, T> resultcombiner){
		
		this.leftSource = leftSource;
		this.rightSource = rightsource;
		this.resultcombiner = resultcombiner;
	
	
		cache =  joinAll(leftSource, rightsource, resultcombiner);
		leftSource.source.addChangeListener(new ChangeListener<L>() {

			@Override
			public void onAdd(L obj) {
				K key = leftSource.accessor.apply(obj);
				Join.this.recalculateKey(key);
			}

			@Override
			public void onRemove(L obj) {
				throw new RuntimeException();				
			}

			@Override
			public void onChange(L obj) {
				throw new RuntimeException();				
			}
		});
		rightSource.source.addChangeListener(new ChangeListener<R>() {

			@Override
			public void onAdd(R obj) {
				K key = rightsource.accessor.apply(obj);
				Join.this.recalculateKey(key);
			}

			@Override
			public void onRemove(R obj) {
				throw new RuntimeException();				
			}

			@Override
			public void onChange(R obj) {
				throw new RuntimeException();				
			}
		});
		
		
	}


	protected void recalculateKey(K key) {
		if (this.cache.containsKey(key)){
			this.fireRemove(this.cache.get(key));
		}
		Collection<L> leftCollection = leftSource.get(key) == null ? Collections.emptyList():  leftSource.get(key);
		Collection<R> rightCollection = rightSource.get(key) == null ? Collections.emptyList():  rightSource.get(key);
		T result =  this.resultcombiner.apply(leftCollection, rightCollection);
		this.cache.put(key,result);
		this.fireAdd(result);			
	}


	@Override
	public Iterator<T> iterator() {
		
		return cache.values().iterator();
	}

	
	

	public static <R,K extends Comparable<K>,V1,V2> Collection<R>  matchJoin(
			Index<V1, K> index1,
			Index<V2, K> index2, 
			BiFunction<Collection<V1>, Collection<V2>, R> resultcombiner) {
		
		
		List<R> joined = new ArrayList<>();
			
		PeekingIterator<Entry<K, Collection<V1>>> left = Iterators.peekingIterator(index1.iteratorKey());
		PeekingIterator<Entry<K, Collection<V2>>> right = Iterators.peekingIterator(index2.iteratorKey());
		
		if (left.hasNext() && right.hasNext()){
			
			Entry<K, Collection<V1>> leftEntry = left.next();
			Entry<K, Collection<V2>> rightEntry = right.next();
			
			if (leftEntry.getKey().equals(rightEntry.getKey())){
				joined.add(resultcombiner.apply(leftEntry.getValue(), rightEntry.getValue()));
			}
			
			while(left.hasNext() || right.hasNext()){
				
				if (left.hasNext() && left.peek().getKey().compareTo(rightEntry.getKey())<=0){
					leftEntry = left.next();
				}else if (right.hasNext() && right.peek().getKey().compareTo(leftEntry.getKey())<=0){
					rightEntry = right.next();
				}else if(right.hasNext()){
					rightEntry = right.next();
				}else if (left.hasNext()){
					leftEntry = left.next();
				}else{
					break;
				}
				
				//System.out.println("Doing "+leftEntry.getKey()+" and "+rightEntry.getKey());
				if (leftEntry.getKey().equals(rightEntry.getKey())){
					joined.add(resultcombiner.apply(leftEntry.getValue(), rightEntry.getValue()));
				}
			}
		}
		return joined;
	}

	
	public static <R,K extends Comparable<K>,V1,V2> Map<K,R>  joinAll(
			Index<V1, K> index1,
			Index<V2, K> index2, 
			BiFunction<Collection<V1>, Collection<V2>, R> resultcombiner) {
		
		
		Map<K, R> joined = new HashMap<>();
			
		Iterator<Entry<K, Collection<V1>>> left = index1.iteratorKey();
		Iterator<Entry<K, Collection<V2>>> right = index2.iteratorKey();
		
		Entry<K, Collection<V1>> leftEntry = nextOrNull(left);
		Entry<K, Collection<V2>> rightEntry = nextOrNull(right);
		
		while (leftEntry != null && rightEntry != null){
			int comp = leftEntry.getKey().compareTo(rightEntry.getKey());
			K key;
			R result;
			if (comp == 0){
				key = leftEntry.getKey();
				result = resultcombiner.apply(leftEntry.getValue(), rightEntry.getValue());
				leftEntry = nextOrNull(left);
				rightEntry = nextOrNull(right);		
			}else if (comp < 0){		
				key = leftEntry.getKey();				
				result = resultcombiner.apply(leftEntry.getValue(), Collections.emptyList());
				leftEntry = nextOrNull(left);				
			}else{
				key = rightEntry.getKey();				
				result = resultcombiner.apply(Collections.emptyList(), rightEntry.getValue());
				rightEntry = nextOrNull(right);						
			}
			joined.put(key, result);	
		}

		
		while (leftEntry != null){
			joined.put(
					leftEntry.getKey(), 
					resultcombiner.apply(leftEntry.getValue(), Collections.emptyList()));
			leftEntry = nextOrNull(left);
		}
		
		while (rightEntry != null){
			joined.put(
					rightEntry.getKey(), 
					resultcombiner.apply(Collections.emptyList(),rightEntry.getValue()));
			rightEntry = nextOrNull(right);
		}
		
		return joined;
	}
	
	
	public static  <T> T nextOrNull (Iterator<T> iter){
		return iter.hasNext() ? iter.next():null;
	}

}
