package stateAlt;

import state.State;

public class AfterExpr<T> extends Tunnel<T> {

	protected AfterExpr(String word, State<String> prev, T next) {
		super(word, prev, next);
	}
	
	public T next(){
		((Tunnel)this.next).setPrev(this);
		return this.next;
	}


}
