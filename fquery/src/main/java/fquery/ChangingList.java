package fquery;

public interface ChangingList<T> extends Iterable<T> {


	void addChangeListener(ChangeListener l);

	

	
	public interface ChangeListener<T> {
			
		void onAdd(T obj);
		void onRemove(T obj);
		void onChange(T obj);
	}
	
}
