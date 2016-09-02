package fquery;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Index<T,A> {
	
	Map<A, T> index = new HashMap<>();
	
	public Index(Tokenizer<T> tokenizer, Function<T, A> accessor, Halde h){
		h.plow(tokenizer).forEachRemaining(obj->index.put(accessor.apply(obj), obj));
		
		h.addContentListener(data ->{
			 tokenizer.tokenize(data)
			 	.forEach(obj->index.put(accessor.apply(obj), obj));
		});
	}
	

	public T get(A key){
		return index.get(key);
	}
	

}
