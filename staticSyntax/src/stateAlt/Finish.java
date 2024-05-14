package stateAlt;

import state.State;

public class Finish<T> extends Tunnel<T> {

	protected Finish(String word, State<String> prev, T next) {
		super(word, prev, next);
	}
	
	public T done(){
		return this.next;
	}

}
