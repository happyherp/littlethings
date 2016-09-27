package fquery;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

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
public class Mapping<S,T> extends AbstractChangingList<T> {
	
	Map<S,T> cache = new HashMap<>();
	
	public Mapping(ChangingList<S> source, Function<S, T> f) {
		source.forEach(s->cache.put(s, f.apply(s)));
		source.addChangeListener(new ChangeListener<S>() {

			@Override
			public void onAdd(S s) {
				cache.put(s, f.apply(s));
			}

			@Override
			public void onRemove(S s) {
				cache.remove(s);
			}

			@Override
			public void onChange(S s) {
				cache.put(s, f.apply(s));				
			}
		});
	}

	@Override
	public Iterator<T> iterator() {
		return cache.values().iterator();
	}

}
