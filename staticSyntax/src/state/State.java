package state;

import java.util.ArrayList;
import java.util.List;


public class State<W> {
	
	W word;
	State<W> prev;
	
	protected State(W word, State<W> prev){
		this.word = word;
		this.prev = prev;		
	}
	
	
	public List<W> getWords(){
		if (this.word == null){
			return new ArrayList<>();
		}else{
			List<W> l = new ArrayList<>(this.prev.getWords());
			l.add(word);
			return l;
		}
	}

	public static  State genericStart(){
		return new State(null, null);
	}
}
