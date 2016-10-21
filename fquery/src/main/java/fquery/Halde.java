package fquery;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.lang3.SerializationUtils;

import fquery.bank.Account;
import fquery.bank.Transfer;


public class Halde extends AbstractChangingView<RawData>{
	
	List<RawData> content = new ArrayList<>();
	
	Flatmapping<RawData, Serializable> serializeReader = Tokenizer.doMap(this, new SerializeTokenizer<Serializable>(Serializable.class));
	
	long counter = 1;
	
	public void write(Serializable serializable){
		this.write(SerializationUtils.serialize(serializable));
	}

	public void write(byte[] data) {
		RawData rawdata = new RawData(data,Instant.now(), this.counter++);
		content.add(rawdata);	
		this.fireAdd(rawdata);
	}

	@Override
	public Iterator<RawData> iterator() {
		return content.iterator();
	}

	public <T extends Serializable> ChangingView<T> get(Class<T> class1) {
		return (ChangingView<T>) serializeReader
				.filter(o->o.getClass().equals(class1));
				
	}
}
