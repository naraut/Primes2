package com.ss.queue;

public class PrimeMessage implements MemoryMappedMessage {

    private final int candidate;

    public PrimeMessage(int candidate) {
        this.candidate = candidate;
    }

    @Override
    public Integer getValue() {
        return candidate;
    }
}
