package fquery;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SerializeTokenizer<T extends Serializable> implements Tokenizer<T>{

	
	private Class<T> clazz;

	public SerializeTokenizer(Class<T> clazz) {
		this.clazz =clazz;
	}
	
	@Override
	public Iterable<T> tokenize(byte[] data) {
		
		List<T> l = new ArrayList<>();
		
		try {
			ObjectInputStream stream = new ObjectInputStream(new ByteArrayInputStream(data));
			while (true){
				try {
					Object o = stream.readObject();
					if (clazz.isInstance(o)) {
						l.add((T) o);
					}
				} catch (EOFException e) {
					break;
				}
			}
			
		} catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		
		return l;
	}

}
