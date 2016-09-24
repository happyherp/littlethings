package fquery;

public interface Tokenizer<T> {
	
	
	Iterable<T> tokenize(RawData data);

}
