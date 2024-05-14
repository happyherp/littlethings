package state.sql;

import state.State;
import state.sql.cond.Expression;

public class MJoinMOn extends MJoin {

	protected MJoinMOn(String word, State<String> prev) {
		super(word, prev);
	}
	
	public MJoin on(Expression e){
		return new MJoin(" on("+e.buildString()+")", this);
	}

}
