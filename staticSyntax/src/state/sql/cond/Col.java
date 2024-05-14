package state.sql.cond;

import state.State;


public class Col extends Expression {

	public Col(String col) {
		super(col, State.genericStart());
	}
	

}
