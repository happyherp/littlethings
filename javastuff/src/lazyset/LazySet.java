package lazyset;

import java.util.Iterator;
import java.util.NavigableSet;
import java.util.TreeSet;

class LazySet<T> {

    private Iterator<T> source;
	private NavigableSet<T> set;

	public LazySet(Iterator<T> source){
         this.source = source;
         this.set = new TreeSet<T>() ;        		 
    }
	
	public boolean contains(T elem){
		// Fetch items from the source into the set until the element(or one greater than it)
		// has been found
		while (this.set.ceiling(elem) == null){
				if (this.source.hasNext()){
					this.set.add(this.source.next());
				}else{
					return false;
				}
		}
		return this.set.contains(elem);
	}    
}