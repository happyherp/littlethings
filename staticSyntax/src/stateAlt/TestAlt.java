package stateAlt;

public class TestAlt {
	
	public static void main(String[] args){
		
		System.out.println(
				new BeforeExpr<String>().atom("x").next().done()
				
				);
		
		System.out.println(
				new BeforeExpr<String>().round().atom("x").next().close().done()
				
				);
		
	}

}
