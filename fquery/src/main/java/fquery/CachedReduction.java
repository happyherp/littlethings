package fquery;

import fquery.ChangingList.ChangeListener;

public class CachedReduction<T, R> {
	
	Reducer<T, R> reducer;
	
	R cachedResult;
	
	public CachedReduction(Reducer<T, R> reducer, ChangingList<T> source) {
		super();
		this.reducer = reducer;
		this.cachedResult = Reducer.reduce(source, reducer);
		source.addChangeListener(new ChangeListener<T>() {
			
			@Override
			public void onRemove(T obj) {
				throw new RuntimeException("Not implemented");
			}
			
			@Override
			public void onChange(T obj) {
				throw new RuntimeException("Not implemented");
			}
			
			@Override
			public void onAdd(T obj) {
				updateCache(obj);
			}
		});
	}
	
	public void updateCache(T d){
		this.cachedResult = reducer.reduce(this.cachedResult, d);
	}

	public R getResult() {
		return this.cachedResult;
	}



}
