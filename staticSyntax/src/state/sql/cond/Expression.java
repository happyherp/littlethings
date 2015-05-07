package state.sql.cond;

import state.State;
import state.StringConstructionState;

public class Expression extends StringConstructionState {

	protected Expression(String word, State<String> prev) {
		super(word, prev);
	}
	
	public Expression isNull(){
		return new Expression(" is null", this);
	}
	
	public AfterOp and(){
		return new AfterOp(" and", this);
	}
	
	public Expression and(Expression e){
		return new Expression(" and "+e.buildString(), this);
	}
	
	public AfterOp or(){
		return new AfterOp(" or", this);
	}
	
	public Expression or(Expression e){
		return new Expression(" or "+e.buildString(), this);
	}
	
	public AfterOp eq(){
		return new AfterOp(" =", this);
	}
	
	public Expression eq(Expression e){
		return new Expression(" = "+e.buildString(), this);
	}
	

}
