package state.sql.cond;

import state.State;
import state.StringConstructionState;

public class AfterOp extends StringConstructionState {

	protected AfterOp(String word, State<String> prev) {
		super(word, prev);
	}
	
	public Expression val(Object o){
		return new Expression(" "+o, this);
	}
	
	public Expression val(String s){
		return new Expression(String.format(" '%s'", s), this);
	}
	
	public Expression col(String col){
		return new Expression(" "+col, this);		
	}
	

}
