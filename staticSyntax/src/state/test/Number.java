package state.test;

import state.State;

public class Number extends State<Integer> {
	
	public Number(){
		super(null, null);
	}

	protected Number(Integer word, State<Integer> prev) {
		super(word, prev);
	}
	
	public Number zero(){
		return new Number(0, this);
	}
	
	public Number one(){
		return new Number(1, this);
	}
	
	public Number two(){
		return new Number(2, this);
	}
	
	public Number three(){
		return new Number(3, this);
	}
	
	public Integer value(){
		int i = 0;
		for (Integer d : this.getWords()){
			i *= 10;
			i += d;
		}
		return i;
	}

}
