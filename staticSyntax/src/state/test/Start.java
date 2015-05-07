package state.test;

import state.State;

public class Start extends State<String>{
	
	protected Start() {
		super(null, null);
	}

	public S1 a(){
		return new S1("a", this);
	}
	
	public End b(){
		return new End("b", this);
	}

}
