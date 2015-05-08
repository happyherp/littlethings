package stateAlt;

import state.State;
import state.StringConstructionState;

public class BeforeExpr <T> extends Tunnel<T>{
	

	public BeforeExpr(){
		this(null, null, null);
	}
	
	protected BeforeExpr(String word, State<String> prev, T next) {
		super(word, prev, next);
		
	}

	public AfterExpr<T> atom(String s){
		return new AfterExpr<T>(s, this, this.next);
	}
	
	public BeforeExpr<RoundClose<T>> round(){
		return new BeforeExpr("(", this, new RoundClose(")", null, this.next));
	}
	
	
}
