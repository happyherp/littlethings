package state;

public class StringConstructionState extends State<String> {

	protected StringConstructionState(String word, State<String> prev) {
		super(word, prev);
	}
	
	public String buildString(){
		 String whole = "";
		 for (String part:this.getWords()){
			 whole += part;
		 }
		 return whole;
	}

}
