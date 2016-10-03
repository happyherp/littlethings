package fquery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class NestedView<T,A extends Comparable<A>> extends AbstractChangingView<NestedView<T,A>. SubView> {
		
		Map<A, SubView> index = new HashMap<>();
		Function<T, A> accessor;
		ChangingView<T> source;
		
		public NestedView(ChangingView<T> source,  Function<T, A> accessor){
			this.source = source;
			this.accessor = accessor;
			
			source.forEach(this::add);
			
			source.addChangeListener(new ChangeListener<T>(){

				@Override
				public void onAdd(T obj) {
					NestedView.this.add(obj);				
				}

				@Override
				public void onRemove(T obj) {
					A key = accessor.apply(obj);
					SubView subview = index.get(key);
					subview.fireRemove(obj);
					if (subview.isEmpty()){
						index.remove(subview);
						fireRemove(subview);
					}else{
						fireChange(subview, subview);
					}
				}
				
			});
		}
		
		private void add(T obj){
			A key = this.accessor.apply(obj);
			if (!this.index.containsKey(key)){
				SubView subview = new SubView();
				this.index.put(key, subview);
				subview.add(obj);			
				this.fireAdd(subview);
			}else{
				SubView subview = this.index.get(key);
				subview.add(obj);
				//Should we really fire a change here?
				this.fireChange(subview, subview);
			}
		}
		
	@Override
	public Iterator<SubView> iterator() {
		return this.index.values().iterator();
	}
	
	public NestedView<T,A>.SubView get(A i) {
		return this.index.get(i);
	}
	
	public class SubView extends AbstractChangingView<T>{

		List<T> cache = new ArrayList<>();

		
		public void add(T value) {
			cache.add(value);
			this.fireAdd(value);
		}

		public boolean isEmpty() {
			return cache.isEmpty();
		}

		@Override
		public Iterator<T> iterator() {
			return cache.iterator();
		}
		
	}


	
}
