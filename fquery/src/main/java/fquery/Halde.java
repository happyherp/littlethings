package fquery;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.lang3.SerializationUtils;


public class Halde extends AbstractChangingList<RawData>{
	
	List<RawData> content = new ArrayList<>();
	
	long counter = 1;
	
	public void read(Serializable serializable){
		this.read(SerializationUtils.serialize(serializable));
	}

	public void read(byte[] data) {
		RawData rawdata = new RawData(data,Instant.now(), this.counter++);
		content.add(rawdata);	
		this.fireAdd(rawdata);
	}

	@Override
	public Iterator<RawData> iterator() {
		return content.iterator();
	}
}
