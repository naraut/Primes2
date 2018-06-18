package com.ss.prime;

import java.math.BigInteger;
import java.util.concurrent.RecursiveTask;

public class PureForkJoinBigInteger extends RecursiveTask<Boolean> {
    private static final long serialVersionUID = -1883115459616562727L;

    private BigInteger candidate;
    private BigInteger from;
    private BigInteger to;
    boolean isPrime = true;

    public PureForkJoinBigInteger(BigInteger candidate, BigInteger start, BigInteger to) {
        this.candidate = candidate;
        this.from = start;
        this.to = to;
    }

    public PureForkJoinBigInteger(String candidate, BigInteger start, BigInteger to) {
        this.candidate = new BigInteger(candidate);
        this.from = start;
        this.to = to;
    }

    private boolean computeDirectly() {
        for (BigInteger i = this.from; i.compareTo(this.to) <= 0; i = i
                .add(BigInteger.ONE)) {
            if (candidate.mod(i).equals(BigInteger.ZERO)) {
                isPrime = false;
            }
        }
        return isPrime;
    }

    @Override
    protected Boolean compute() {
        BigInteger tmp = to.subtract(from);
        if (tmp.compareTo(Utils.NumberForThread) <= 0) {
            return computeDirectly();
        }

        BigInteger middle = to.add(from).divide(Utils.two);
        PureForkJoinBigInteger leftJoin = new PureForkJoinBigInteger(candidate, this.from, middle);
        PureForkJoinBigInteger rightJoin = new PureForkJoinBigInteger(candidate, middle.add(BigInteger.ONE),
                this.to);

        invokeAll(leftJoin, rightJoin);

        return leftJoin.isPrime && rightJoin.isPrime;
    }
}
