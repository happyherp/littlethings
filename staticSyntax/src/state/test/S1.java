package state.test;

import state.State;

public class S1 extends State<String> {
	
	protected S1(String w, State<String> prev){
		super(w, prev);
	}
	
	public S2 c(){
		return new S2("c", this);
	}

	public End b() {
		return new End("b", this);
		
	}

}
