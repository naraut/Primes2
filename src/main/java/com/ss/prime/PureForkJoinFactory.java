package com.ss.prime;

public class PureForkJoinFactory {

    public static PrimeForkJoinTask createCP(int candidate)
    {
        return new PrimeForkJoinTask(candidate, 5, (int) Math.sqrt((double) candidate));
    }
}
