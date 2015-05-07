package state.sql;

import java.sql.ResultSet;

import state.State;

public class OrderDir extends MMoreOrder {

	protected OrderDir(String word, State<String> prev) {
		super(word, prev);
	}
	
	public MMoreOrder desc(){
		return new MMoreOrder(" desc", this);
	}
	
	public MMoreOrder asc(){
		return new MMoreOrder(" asc", this);
	}

}
