package state.sql.cond;

public class SQLCondition {

	
	private String sql;

	protected SQLCondition(String sql){
		this.sql = sql;
	}
	
	public String getSQL(){
		return sql;
	}
}
