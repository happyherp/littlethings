package fquery;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public interface Reducer<T,R> {

	R initial();
	
	R reduce(R r, T t);
	
	R combine(R r1, R r2);
	
	static <T,R> R reduce(ChangingView<T> source, Reducer<T, R> reducer) {
		
		return  source.stream()
			.reduce(reducer.initial(), reducer::reduce, reducer::combine);		
	}

	public static <R,T> Reducer<T, R> build(Supplier<R> supplier, BiFunction<T, R, R> reducer, BiFunction<R, R, R> combiner){
		return new Reducer<T, R>() {

			@Override
			public R initial() {
				return supplier.get();
			}

			@Override
			public R reduce(R r,T t) {
				return reducer.apply(t, r);
			}

			@Override
			public R combine(R r1, R r2) {
				return combiner.apply(r1, r2);
			}
		};
	}
	
	
}
