package state.sql;

import java.sql.ResultSet;

import state.State;
import state.sql.cond.Expression;

public class MJoin extends SQLSelectState {

	protected MJoin(String word, State<String> prev) {
		super(word, prev);
	}
	
	public MJoinMOn join(String table){ 
		return new MJoinMOn(" join "+table , this);
	}
	
	public MOrder where(Expression cond) {
		return new MOrder(" where "+cond.buildString(), this);
	}
	
	@Override
	public ResultSet fetch(){
		return super.fetch();
	}

}
