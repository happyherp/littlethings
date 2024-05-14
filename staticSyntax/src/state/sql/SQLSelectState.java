package state.sql;

import java.sql.ResultSet;

import state.State;
import state.StringConstructionState;

abstract class SQLSelectState extends StringConstructionState {

	protected SQLSelectState(String word, State<String> prev) {
		super(word, prev);
	}
	

	/**
	 * Override and set to public for all states that might be last.
	 * 
	 * @return
	 */
	protected ResultSet fetch(){
		System.out.println("Doing: "+this.buildString());
		return null;
	}

}
