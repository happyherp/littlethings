package wikipedia;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {
    /*
     * Complete the function below.
     * https://en.wikipedia.org/w/api.php?action=parse&section=0&prop=text&format=json&page=[topic]
     */

    static int getTopicCount(String topic) throws Exception {

        URL url = new URL("https://en.wikipedia.org/w/api.php?action=parse&section=0&prop=text&format=json&page="
                + URLEncoder.encode(topic, "utf-8"));

        URLConnection connection = url.openConnection();

        //System.out.println("Content: "+connection.getContentEncoding());


        Pattern regExpPattern = Pattern.compile(Pattern.quote(topic));

        Response page = new Gson().fromJson(new InputStreamReader(connection.getInputStream()), Response.class);
        int occurrences = page.parse.text.values().stream().mapToInt(text -> {
            int i = 0;
            Matcher m = regExpPattern.matcher(text);
            while (m.find()) {
                i++;
            }
            return i;
        }).sum();


        return occurrences;
    }

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        final String fileName = System.getenv("OUTPUT_PATH");
        BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
        int res;
        String _topic;
        try {
            _topic = in.nextLine();
        } catch (Exception e) {
            _topic = null;
        }

        try {
            res = getTopicCount(_topic);
        } catch (Exception e) {
            res = 0;
        }
        bw.write(String.valueOf(res));
        bw.newLine();

        bw.close();
    }

    public static class Response {
        public Page parse;
    }

    public static class Page {
        public Map<String, String> text;
    }

}