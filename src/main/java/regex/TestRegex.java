package regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestRegex {
    public static void main(String[] args) {
        String pattern="\\?:";
//        pattern.replaceAll("\\?:", "");
        Pattern compile = Pattern.compile(pattern);
        Matcher matcher = compile.matcher("([^@]+)@([^@]+)");
        System.out.println(matcher.find());


        String s = "?:";
        s= s.replaceAll("\\?:", "xxx");
        System.out.println(s);

    }
}
