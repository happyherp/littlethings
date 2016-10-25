package fquery;

public interface Reduction2<T,R> extends Reducer<T,R> {
	
	R subtract(R r, T t);

	
	public static <T> Reduction2<T, Integer> counter(){
		return new Reduction2<T, Integer>() {

			@Override
			public Integer initial() {
				return 0;
			}

			@Override
			public Integer reduce(Integer r, T t) {
				return r+1;
			}

			@Override
			public Integer combine(Integer r1, Integer r2) {
				return r1+r2;
			}

			@Override
			public Integer subtract(Integer r, T t) {
				return r-1;
			}
		};
	}
	
}
