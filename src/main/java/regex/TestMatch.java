package regex;

import com.fulmicoton.multiregexp.MultiPattern;
import com.fulmicoton.multiregexp.MultiPatternMatcher;

public class TestMatch {
    public static void main(String[] args){
        MultiPatternMatcher matcher = MultiPattern.of(
                "ab+",     // 0
                "abc+",    // 1
                "ab?c",    // 2
                "v",       // 3
                "v.*",     // 4
                "(def)+"   // 5
                ,"(^978)?\\d{9}[1-9X]$"
                ,"^ZL\\s?(\\d{2}|\\d{4})[12389](\\d{5}|\\d{7})\\.[0-9X]$"
        ).matcher();
        int[] matching = matcher.match("ZL 201610032112.4"); // return {1, 2}
        for (int j : matching) {
            System.out.println(j);
        }
    }
}
