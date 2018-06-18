package com.ss.prime;

import java.math.BigInteger;

public class PureForkJoinFactory {
    public static PureForkJoin create(int candidate)
    {
        return new PureForkJoin(candidate, 5, (int) Math.sqrt((double) candidate));
    }

    public static PureForkJoinBigInteger createBigInteger(String candidate)
    {
        BigInteger biCn = new BigInteger(candidate);
        return new PureForkJoinBigInteger(biCn, new BigInteger("2"), Utils.sqrt(biCn));
    }

    public static PureForkJoinCP createCP(int candidate)
    {
        PureForkJoinCP pureForkJoinCP = new PureForkJoinCP(candidate, 5, (int) Math.sqrt((double) candidate));
        return pureForkJoinCP;
    }

}
