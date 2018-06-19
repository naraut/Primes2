package com.ss.queue;

public class ResultMessage implements MemoryMappedMessage {

    private final int candidate;
    private final boolean isPrime;

    public ResultMessage(int candidate, boolean isPrime) {
        this.candidate = candidate;
        this.isPrime = isPrime;
    }

    @Override
    public Integer getValue() {
        return candidate;
    }

    public boolean isPrime() {
        return isPrime;
    }
}
