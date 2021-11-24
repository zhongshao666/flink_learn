package regex;

import com.fulmicoton.multiregexp.MultiPattern;
import com.fulmicoton.multiregexp.MultiPatternSearcher;

public class TestSearch {
    public static void main(String[] args){
        MultiPatternSearcher multiPatternSearcher = MultiPattern.of(
                "ab+",     // 0
                "abc+",    // 1
                "ab?c",    // 2
                "v",       // 3
                "v.*",     // 4
                "(def)+"   // 5
        ).searcher();
        MultiPatternSearcher.Cursor cursor = multiPatternSearcher.search("ab abc vvv");
        while (cursor.next()) {
//            int[] pattern = cursor.match();   // array with the pattern id which match ends at this position
            int pattern = cursor.match();
            int start = cursor.start();
            int end = cursor.end();
            System.out.print(pattern+"\t");
            System.out.print(start+"\t");
            System.out.print(end+"\n");
        }
    }
}
