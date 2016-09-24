package fquery;

public class CachedReduction<T, R> {
	
	Reducer<T, R> reducer;
	Tokenizer<T> tokenizer;
	Halde halde;
	
	
	R cachedResult;
	
	public CachedReduction(Reducer<T, R> reducer, Tokenizer<T> tokenizer, Halde halde) {
		super();
		this.reducer = reducer;
		this.tokenizer = tokenizer;
		this.halde = halde;
		this.cachedResult = this.halde.reduce(tokenizer, reducer);
		this.halde.addContentListener(this::updateCache);
	}
	
	public void updateCache(RawData d){
		
		this.cachedResult = StreamUtils.asStream(tokenizer.tokenize(d).iterator())
				.reduce(this.cachedResult, reducer::reduce, reducer::combine);
	}

	public R getResult() {
		return this.cachedResult;
	}

}
