package fquery;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SimpleChangingSet<T> extends AbstractChangingView<T>  {

	Set<T> list = new HashSet<>();
	
	@Override
	public Iterator<T> iterator() {
		return list.iterator();
	}


}
