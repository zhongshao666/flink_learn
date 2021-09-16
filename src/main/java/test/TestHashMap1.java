package test;

import java.util.HashMap;
import java.util.Scanner;

public class TestHashMap1 {
    public static void main(String[] args) {
        System.out.println("1   5   9   13  17  21  25  29  33");
        new HashMap<>();
        String s = "HashMap";
        int a = s.hashCode();
        int b = a >>> 16;
//        printBin(a);
//        printBin(b);
//        printBin(a^b);
        printBin(15);
        printBin(-15);

//        Scanner scan = new Scanner(System.in);
//        System.out.println("请输入一个整数:");
//        int number = scan.nextInt();

    }
    public static void printBin(int number) {
        int tmp = 1<<31; //用于计算的临时变量

        System.out.println();
        for(int i=0; i<Integer.SIZE-1; i++) {
            //System.out.print(number &(tmp>>>=1));
            System.out.print( (number &(tmp>>>=1))>0? 1:0);
        }
        System.out.println();

    }
}
