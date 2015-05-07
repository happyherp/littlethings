package state.sql;

import state.State;

public class OneColumn extends SQLSelectState {

	protected OneColumn(String word, State<String> prev) {
		super(word, prev);
	}
	
	public ColOrFrom c(String name){
		return new ColOrFrom(" "+name, this);
	}

}
