package fquery;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 
 * Does not care about ordering.
 * Works like a set, not a list. 
 * 
 * @author Hamburger
 *
 * @param <S>
 * @param <T>
 */
public class Flatmapping<S,T> extends AbstractChangingList<T> {
	
	Map<S,Collection<T>> cache = new HashMap<>();
	
	public Flatmapping(ChangingList<S> source, Function<S, Collection<T>> f) {
		source.forEach(s->cache.put(s, f.apply(s)));
		source.addChangeListener(new ChangeListener<S>() {

			@Override
			public void onAdd(S s) {
				Collection<T> mapped = f.apply(s);
				cache.put(s, mapped);
				mapped.forEach(Flatmapping.this::fireAdd);
			}

			@Override
			public void onRemove(S s) {
				cache.remove(s)
					.forEach(Flatmapping.this::fireAdd);
			}

			@Override
			public void onChange(S s) {
				onAdd(s);			
			}
		});
	}

	@Override
	public Iterator<T> iterator() {
		return cache.values().stream()
				.flatMap(Collection::stream)
				.collect(Collectors.toList())
				.iterator();
	}

}
