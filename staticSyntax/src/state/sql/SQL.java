package state.sql;

import state.State;

public class SQL extends SQLSelectState {
	
	public SQL(){
		this(null, null);
	}

	protected SQL(String word, State<String> prev) {
		super(word, prev);
	}
	
	public OneColumn select(){
		return new OneColumn("select", this);
	}

}
