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
        ).matcher();
        int[] matching = matcher.match("abc"); // return {1, 2}
        for (int j : matching) {
            System.out.println(j);
        }
    }
}
