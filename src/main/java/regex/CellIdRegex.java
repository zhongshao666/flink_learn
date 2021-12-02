package regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CellIdRegex {
    public static void main(String[] args) {

        String pattern = "^([0-9]|[1-9]\\d|[1-9]\\d{2}|[1-9]\\d{3}|[1-5]\\d{4}|6[0-4]\\d{3}|65[0-4]\\d{2}|655[0-2]\\d|6553[0-5])$";
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher("64588");
        System.out.println(matcher.matches());
    }
}
