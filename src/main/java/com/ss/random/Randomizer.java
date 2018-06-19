package com.ss.random;

import com.ss.queue.MemoryMappedQueue;

import java.util.Random;

public class Randomizer {
    private final Random random;
    private final int bound = Integer.MAX_VALUE;

    public Randomizer() {
        this.random = new Random();
    }

    public static void main(String[] args) throws Exception {
        Randomizer randomizer = new Randomizer();
        randomizer.sendRandom();
    }

    private void sendRandom() throws Exception {
        long time = System.currentTimeMillis();
        boolean stop = false;
        int counter=0;
        try (MemoryMappedQueue queue = new MemoryMappedQueue("prime-queue", 2))
        {
            while(!stop)
            {
                queue.offer(random.nextInt(bound));
                counter++;
                if(counter == 150_000){
                    stop = true;
                    System.out.printf("Sent %d messages\n", counter);
                }
                Thread.sleep(10);
            }
        }finally {
            System.out.printf("Time taken for sending: %d messages was : %d ms\n", counter, (System.currentTimeMillis() - time));
        }
    }
}
