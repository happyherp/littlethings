package fquery;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.lang3.SerializationUtils;


public class Halde {
	
	List<RawData> content = new ArrayList<>();
	
	long counter = 1;
	
	List<Consumer<RawData>> onNewContent = new ArrayList<>();
	
	public void addContentListener(Consumer<RawData> listener){
		this.onNewContent.add(listener);
	}
	
	public void read(Serializable serializable){
		this.read(SerializationUtils.serialize(serializable));
	}

	public void read(byte[] data) {
		RawData rawdata = new RawData(data,Instant.now(), this.counter++);
		content.add(rawdata);	
		onNewContent.forEach(c->c.accept(rawdata));
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
