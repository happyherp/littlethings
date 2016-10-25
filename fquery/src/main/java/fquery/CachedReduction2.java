package fquery;

import fquery.ChangingView.ChangeListener;

public class CachedReduction2<T, R> {
	
	Reduction2<T, R> reducer;
	
	R cachedResult;
	
	public CachedReduction2(Reduction2<T, R> reducer, ChangingView<T> source) {
		super();
		this.reducer = reducer;
		this.cachedResult = Reducer.reduce(source, reducer);
		source.addChangeListener(new ChangeListener<T>() {
			
			@Override
			public void onRemove(T obj) {
				cachedResult = reducer.subtract(cachedResult, obj);
			}
			
			@Override
			public void onAdd(T obj) {
				cachedResult = reducer.reduce(cachedResult, obj);
			}
		});
	}
	

	public R getResult() {
		return this.cachedResult;
	}



}
