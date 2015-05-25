package state.sql;

import java.sql.ResultSet;

import state.State;

public class MOrder extends SQLSelectState {

	protected MOrder(String word, State<String> prev) {
		super(word, prev);
	}
	
	public OrderDir orderBy(String field){
		return new OrderDir(" order by "+field, this);
	}
	
	@Override
	public ResultSet fetch(){
		return super.fetch();
	}

}
