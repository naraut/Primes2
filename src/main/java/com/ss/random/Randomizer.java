package com.ss.random;

import com.ss.queue.MemoryMappedQueue;

import java.util.Random;

public class Randomizer {
    private final MemoryMappedQueue queue;
    private final Random random;
    private final int bound = Integer.MAX_VALUE;

    public Randomizer() throws Exception {
        queue = new MemoryMappedQueue("mm-queue", 2);
        this.random = new Random();
    }

    public static void main(String[] args) throws Exception {
        long time = System.currentTimeMillis();
        Randomizer randomizer = new Randomizer();
        while(true)
        {
            randomizer.sendRandom();
        }
//        System.out.printf("Time taken: %d \n", (System.currentTimeMillis() - time));
    }

    private void sendRandom() {
        queue.offer(random.nextInt(bound));
    }
}
