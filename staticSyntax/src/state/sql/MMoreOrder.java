package state.sql;

import java.sql.ResultSet;

import state.State;

public class MMoreOrder extends SQLSelectState {

	protected MMoreOrder(String word, State<String> prev) {
		super(word, prev);
	}
	
	public OrderDir c(String field){
		return new OrderDir(", "+field, this);
	}
	
	
	@Override
	public ResultSet fetch(){
		return super.fetch();
	}

}
