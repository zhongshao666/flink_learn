package regex.BenchMark;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class TestMultiRegex {
    public static void main(String[] args) throws IOException {
        final String txtFilePath = "data/cep.txt";
        final String patternFilePath = "data/regex.txt";

        final String txt = new String(Files.readAllBytes(Paths.get(txtFilePath)), StandardCharsets.UTF_8);
        final List<String> patterns = Files.readAllLines(Paths.get(patternFilePath), StandardCharsets.UTF_8);
        System.out.println(txt.length());
        patterns.forEach(s->{
            System.out.println("-----");
            System.out.println(s);
        });


        while (true) {
            for (final PatternMethods patternMethod: PatternMethods.values()) {
                System.out.println("---------------------------");
                System.out.println(patternMethod.name());
                long start = System.currentTimeMillis();
                final PatternMethods.PatternMatchingMethod method = patternMethod.make(patterns);
                long end = System.currentTimeMillis();
                System.out.println("build time (ms):   " + (end - start));
                {
                    start = System.currentTimeMillis();
                    final int[] patternCounts = method.matchCounts(txt);
                    end = System.currentTimeMillis();
                    System.out.println("match time (ms):   " + (end - start));
                    for (int patternCount: patternCounts) {
                        System.out.print(patternCount + " ");
                    }
                    System.out.println();
                }
            }
        }



    }
}
