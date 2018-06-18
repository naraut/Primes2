package com.ss.prime;

import java.util.concurrent.RecursiveTask;

public class PureForkJoin extends RecursiveTask<Boolean> {


    private static final long serialVersionUID = -1883115459616562727L;

    private final int candidate;
    private int from;
    private int to;
    private boolean isPrime = true;

    PureForkJoin(int candidate, int from, int to) {
        this.candidate = candidate;
        this.from = from;
        this.to = to;
    }

    private boolean computeDirectly() {
        //skip even number
        for (int i = this.from; i<this.to ; i++) {// iterate to skip even numbers
            if (candidate%i == 0) {
                isPrime = false;
            }
        }
        return isPrime;
    }

    @Override
    protected Boolean compute() {

        int tmp = to - from ;
        if (tmp < 200) {
            return computeDirectly();
        }

        int middle = (to+from)/2;
        PureForkJoin leftJoin = new PureForkJoin(candidate, from, middle);
        PureForkJoin rightJoin = new PureForkJoin(candidate, middle++, to);

        invokeAll(leftJoin, rightJoin);

        return leftJoin.isPrime() && rightJoin.isPrime();
    }

    protected boolean isPrime()
    {
        return isPrime;
    }
}