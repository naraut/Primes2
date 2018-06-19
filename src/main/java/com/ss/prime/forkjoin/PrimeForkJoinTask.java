package com.ss.prime.forkjoin;

import com.ss.prime.Primes;

import java.util.concurrent.RecursiveTask;

public class PrimeForkJoinTask extends RecursiveTask<Boolean> {

    private static final long serialVersionUID = -1883115459616562727L;

    private final int candidate;
    private int from;
    private int to;

    PrimeForkJoinTask(int candidate, int from, int to) {
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
        int length = to - from ;
        if (length < Primes.computeDirectlyLength()) {
            return computeDirectly();
        }

        int middle = (to+from)/2;
        PrimeForkJoinTask leftHalf = new PrimeForkJoinTask(candidate, from, middle);
        PrimeForkJoinTask rightHalf = new PrimeForkJoinTask(candidate, middle++, to);

        invokeAll(leftHalf, rightHalf);
        return !Primes.isComplete(candidate);
    }
}