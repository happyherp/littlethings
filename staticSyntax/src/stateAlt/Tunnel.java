package stateAlt;

import state.State;
import state.StringConstructionState;

public class Tunnel<T> extends StringConstructionState {

	protected T next;

	protected Tunnel(String word, State<String> prev, T next) {
		super(word, prev);
		this.next = next;
	}
	
	protected void setPrev(State prev){
		this.prev = prev;
	}

}
