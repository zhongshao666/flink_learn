package regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookRegex {
    public static void main(String[] args) {

        String s="978-7-115-47258-8";
        s = s.replace("-", "");

        String pattern = "(^978)?\\d{9}[1-9X]$";
        System.out.println(pattern);
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(s);// 7508027108 9787302551966  7-5600-3879-4
        System.out.println(matcher.find());
        System.out.println(checkBook("978-7-01-023558-5".replace("-","")));
    }

    private static final int[] arr9 = {10,9,8,7,6,5,4,3,2};
    private static final int[] arr12 = {1,3,1,3,1,3,1,3,1,3,1,3};
    public static boolean checkBook(String book) {
        System.out.println(book.length());
        int sum =0;
        if (book.length() > 10) {
            String[] split = book.split("\\s*");
            for (int i = 0; i < split.length-1; i++) {
                sum += Integer.parseInt(split[i]) * arr12[i];
            }
            int s=10-sum%10;
            if (s == 10) {
                return "X".equals(split[12]);
            }else return String.valueOf(s).equals(split[12]);
        }else {
            String[] split = book.split("\\s*");
            for (int i = 0; i < split.length-1; i++) {
                sum += Integer.parseInt(split[i]) * arr9[i];
            }
            int r = sum % 11;
            if (r == 0) {
                return "0".equals(split[9]);
            } else if (r == 1) {
                return "X".equals(split[9]);
            }
            else return String.valueOf(11-r).equals(split[9]);
        }

    }
}
