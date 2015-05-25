package state.test;

import state.State;

public class S2 extends State<String> {

	protected S2(String word, State<String> prev) {
		super(word, prev);
	}
	
	
	public S1 a(){
		return new S1("a", this);
	}
	
	public S2 b(){
		return new S2("b", this);
	}

}
