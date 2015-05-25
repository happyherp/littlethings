package state.sql;

import state.State;

public class ColOrFrom extends SQLSelectState {

	protected ColOrFrom(String word, State<String> prev) {
		super(word, prev);
	}
	
	public  ColOrFrom and(String colname){
		return new ColOrFrom(", "+colname, this);
	}
	
	public MJoin from(String table){
		return new MJoin(" from "+table, this);
	}

}
