package fquery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.lang3.SerializationUtils;


public class Halde {
	
	List<byte[]> content = new ArrayList<>();
	
	List<Consumer<byte[]>> onNewContent = new ArrayList<>();
	
	public void addContentListener(Consumer<byte[]> listener){
		this.onNewContent.add(listener);
	}
	
	public void read(Serializable serializable){
		this.read(SerializationUtils.serialize(serializable));
	}

	public void read(byte[] string) {
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
