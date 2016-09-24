package fquery;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractChangingList<T> implements ChangingList<T>{
	
	List<ChangeListener> listeners = new ArrayList<>();

	@Override
	public void addChangeListener(fquery.ChangingList.ChangeListener l) {
		this.listeners.add(l);
	}	
	
	
	

}
