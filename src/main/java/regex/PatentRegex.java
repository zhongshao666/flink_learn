package regex;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatentRegex {
    public static void main(String[] args) {

        String p = "^ZL\\s?(\\d{2}|\\d{4})[12389](\\d{5}|\\d{7})\\.[0-9X]$";

        Pattern pattern = Pattern.compile(p);

        Matcher matcher = pattern.matcher("ZL 201610032112.4");//ZL95115608.X    ZL201610032112.4

        System.out.println(matcher.find());

        checkPatent("ZL95115608.X");

    }

    private static final int[] arr8 = {2,3,4,5,6,7,8,9};
    private static final int[] arr12 = {2,3,4,5,6,7,8,9,2,3,4,5};
    public static boolean checkPatent(String patent) {
        int sum=0;
        if (patent.length() > 12) {
            System.out.println("new patent");
            String[] split = patent.split("\\s*");
            for (int i = 2; i < split.length-2; i++) {
                sum+=Integer.parseInt(split[i])*arr12[i-2];
            }
            int s=sum%11;
            if(s>=10){
                return "X".equals(split[15]);
            }else
                return String.valueOf(s).equals(split[15]);

        } else {
            System.out.println("old patent");
            String[] split = patent.split("\\s*");
            for (int i = 2; i < split.length-2; i++) {
                sum+=Integer.parseInt(split[i])*arr8[i-2];
            }
            System.out.println(sum%11);
            int s=sum%11;
            if(s>=10){
                return "X".equals(split[11]);
            }else
                return String.valueOf(s).equals(split[11]);
        }
    }
}
