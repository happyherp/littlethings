package fquery;

import java.util.Iterator;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class Filter<T> extends AbstractChangingView<T> {

	Set<T> cache;
	
	public Filter(ChangingView<T> source, Predicate<T> test){
		cache =  source.stream()
				.filter(test)
				.collect(Collectors.toSet());
		
		
		source.addChangeListener(new ChangeListener<T>() {

			@Override
			public void onAdd(T obj) {
				if (test.test(obj)){
					cache.add(obj);
					Filter.this.fireAdd(obj);
				}
			}

			@Override
			public void onRemove(T obj) {
				if (test.test(obj)){
					cache.remove(obj);
					Filter.this.fireRemove(obj);
				}				
			}
		});
		
	}


	@Override
	public Iterator<T> iterator() {
		return cache.iterator();
	}

}
