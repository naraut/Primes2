package com.ss.prime;

public class PureForkJoinFactory {

    public static PrimeForkJoinTask create(int candidate)
    {
        return new PrimeForkJoinTask(candidate, 5, (int) Math.sqrt((double) candidate));
    }
}
