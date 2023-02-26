package regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestRegex {
    public static void main(String[] args) {

        //  ?:是你必须要有，但我匹配到不返回
        String pattern="\\?:";
//        pattern.replaceAll("\\?:", "");
        Pattern compile = Pattern.compile(pattern);
        Matcher matcher = compile.matcher("([^@]+)@([^@]+)");
        System.out.println(matcher.find());


        String s = "?:";
        s= s.replaceAll("\\?:", "xxx");
        System.out.println(s);

        System.out.println("-----------------------");
//        Pattern compile1 = Pattern.compile("(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)");
//        Matcher matcher1 = compile1.matcher("1999-01-18");//15621212
//        System.out.println(matcher1.matches());

        Pattern compileD = Pattern.compile("\\D");
        Matcher matcherD = compileD.matcher("我");
        System.out.println(matcherD.matches());
    }
}
