package fquery;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractChangingView<T> implements ChangingView<T>{
	
	List<ChangeListener<T>> listeners = new ArrayList<>();

	@Override
	public void addChangeListener(fquery.ChangingView.ChangeListener<T> l) {
		this.listeners.add(l);
	}	
	
	protected void fireAdd(T t){
		this.listeners.forEach(l->l.onAdd(t));
	}
	
	
	protected void fireRemove(T t) {
		this.listeners.forEach(l->l.onRemove(t));		
	}
	
	protected void fireChange(T old, T newO) {
		this.listeners.forEach(l->l.onChange(old, newO));		
	}

}
