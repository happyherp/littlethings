package fquery;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserTokenizer implements Tokenizer<User> {

	@Override
	public Iterable<User> tokenize(String data) {

		Pattern userpattern = Pattern.compile("<NEWUSER name='([\\w]+)' age='(\\d+)' />");
		Matcher m = userpattern.matcher(data);

		List<User> users = new ArrayList<>();

		while (m.find()) {
			User user = new User();
			user.setName(m.group(1));
			user.setAge(Integer.parseInt(m.group(2)));
			users.add(user);
		}
		return users;
	}

}
