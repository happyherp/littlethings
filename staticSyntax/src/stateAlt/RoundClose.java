package stateAlt;

import state.State;

public class RoundClose<T> extends Tunnel {

	protected RoundClose(String word, State prev, T next) {
		super(word, prev, next);
	}
	
	public Finish<T> close(){
		return new Finish<T>(")", this, (T) next);
	}

}
