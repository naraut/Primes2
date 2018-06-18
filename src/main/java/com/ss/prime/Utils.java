package com.ss.prime;

import java.math.BigInteger;

public class Utils {
    public static BigInteger two = new BigInteger("2");
    public static BigInteger three = new BigInteger("3");

    public static String fileLocation = "/tmp/test.txt";
    public static long numberLinePerThread = 10000;
    public static long lineNum = 1838200;
    public static BigInteger NumberForThread = new BigInteger("200");

    public static BigInteger sqrt(BigInteger n) {
        BigInteger a = BigInteger.ONE;
        BigInteger b = new BigInteger(n.shiftRight(5).add(new BigInteger("8"))
                .toString());
        while (b.compareTo(a) >= 0) {
            BigInteger mid = new BigInteger(a.add(b).shiftRight(1).toString());
            if (mid.multiply(mid).compareTo(n) > 0)
                b = mid.subtract(BigInteger.ONE);
            else
                a = mid.add(BigInteger.ONE);
        }
        return a.subtract(BigInteger.ONE);
    }

    public static boolean isEmptyString(String s) {
        return s == null || "".equals(s.trim());
    }
}