package com.ss.prime;

import java.util.concurrent.RecursiveTask;

public class PureForkJoinCP extends RecursiveTask<Boolean> {

    private static final long serialVersionUID = -1883115459616562727L;

    private final int candidate;
    private int from;
    private int to;

    PureForkJoinCP(int candidate, int from, int to) {
        this.candidate = candidate;
        this.from = from;
        this.to = to;
    }

    private boolean computeDirectly() {
        //skip even number
        if((this.from & 1) == 0) {
            this.from=this.from+1;
        }
        for (int i = this.from; i<this.to ; i=i+2) {// iterate to skip even numbers
            if (candidate%i == 0) {
                Primes.complete(candidate);
                return false;
            }
        }
        return true;
    }

    @Override
    protected Boolean compute() {
        if(Primes.isComplete(candidate)) {
            return false;
        }
        int tmp = to - from ;
        if (tmp < 200) {
            return computeDirectly();
        }

        int middle = (to+from)/2;
        PureForkJoinCP leftJoin = new PureForkJoinCP(candidate, from, middle);
        PureForkJoinCP rightJoin = new PureForkJoinCP(candidate, middle++, to);

        invokeAll(leftJoin, rightJoin);
        return !Primes.isComplete(candidate);
    }
}