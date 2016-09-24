package fquery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.BiFunction;

import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;

public class Join {

	public static <R,K extends Comparable<K>,V1,V2> Collection<R>  join(
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
				
				System.out.println("Doing "+leftEntry.getKey()+" and "+rightEntry.getKey());
				if (leftEntry.getKey().equals(rightEntry.getKey())){
					joined.add(resultcombiner.apply(leftEntry.getValue(), rightEntry.getValue()));
				}
			}
		}
		return joined;
	}

	
}
