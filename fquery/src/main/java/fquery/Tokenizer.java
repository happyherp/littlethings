package fquery;

import com.google.common.collect.Lists;

public interface Tokenizer<T> {
	
	
	Iterable<T> tokenize(RawData data);

	
	public static <T>  Flatmapping<RawData, T> doMap(ChangingView<RawData> halde, Tokenizer<T> tokenizer){
		return new Flatmapping<RawData, T>(halde, d -> Lists.newArrayList(tokenizer.tokenize(d)));
	}
}
