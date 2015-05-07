package state.test;

import state.State;

public class End extends State<String> {

	protected End(String word, State<String> prev) {
		super(word, prev);
	}

}
