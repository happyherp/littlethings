package fquery;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;


public class Halde {
	
	List<String> content = new ArrayList<>();
	
	List<Consumer<String>> onNewContent = new ArrayList<>();
	
	public void addContentListener(Consumer<String> listener){
		this.onNewContent.add(listener);
	}

	public void read(String string) {
		content.add(string);	
		onNewContent.forEach(c->c.accept(string));
	}

	public <T> Iterator<T>  plow(Tokenizer<T> tokenizer) {
		
		return this.content.stream()
			.flatMap(s -> StreamUtils.asStream(tokenizer.tokenize(s).iterator()))
			.iterator();
		
	}

	public  <T,R> R reduce(Tokenizer<? extends T> tokenizer, Reducer<T, R> reducer) {
		
		
		return StreamUtils.asStream(this.plow(tokenizer))
			.reduce(reducer.initial(), reducer::reduce, reducer::combine);
		
	}
}
