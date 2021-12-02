package regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BankRegex {
    public static void main(String[] args) {
        String pattern = "^[1-9]\\d{11,18}$";
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher("6225760009229525");
        System.out.println(matcher.matches());
    }
}
