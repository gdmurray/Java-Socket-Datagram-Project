/**
 * Created by gregmurray on 2019-03-07.
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class temp {
    public static void main(String[] args) {
        String stringToSearch = "<transmission MDS=2048>";
        String testString = "<transmission MDS=2048>";
        String handshakePattern = "<transmission MDS=(\\d+)>";
        Pattern p = Pattern.compile(handshakePattern);   // the pattern to search for
        Matcher m = p.matcher(stringToSearch);
        System.out.println(testString.matches(handshakePattern));
        if (m.find()) {
            String theGroup = m.group(1);

            // print the group out for verification
            System.out.println(theGroup);
        }
    }
}

