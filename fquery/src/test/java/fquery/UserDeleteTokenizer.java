package fquery;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserDeleteTokenizer implements Tokenizer<Userdeletion> {


	@Override
	public Iterable<Userdeletion> tokenize(RawData data) {
		
		
		Pattern userpattern = Pattern.compile("<DELUSER name='([\\w]+)' />");
		
		List<Userdeletion> users = new ArrayList<>();
		for (String stringData : new SerializeTokenizer<>(String.class).tokenize(data)){
			Matcher m = userpattern.matcher(stringData);
			while (m.find()) {
				Userdeletion user = new Userdeletion();
				user.setName(m.group(1));
				users.add(user);
			}			
		}
		return users;

	}

}
