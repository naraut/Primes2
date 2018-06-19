package com.ss.random;

import com.ss.queue.*;

import java.util.Random;
import java.util.concurrent.Executors;

public class Randomizer {
    private final Random random;
    private final int bound = Integer.MAX_VALUE;
    private final MemoryMappedGenericQueue<ResultMessage> resultQueue;

    public Randomizer() throws Exception {
        this.random = new Random();
        resultQueue = new MemoryMappedGenericQueue("prime-queue", 2, ResultMessage.class);
    }

    public static void main(String[] args) throws Exception {
        Randomizer randomizer = new Randomizer();
        randomizer.sendRandom();
        randomizer.receiveResults();
    }

    private void receiveResults() {
        Executors.newSingleThreadExecutor().execute(getResult());
    }

    public Runnable getResult() {
        return () -> {
            while(true) {
                ResultMessage resultMessage = (ResultMessage) resultQueue.poll();
                System.out.printf("[%s] %d is %s\n",Thread.currentThread().getName(), resultMessage.getValue(),
                                                                (resultMessage.isPrime()?"prime":"composite"));
            }
        };
    }

    private void sendRandom() throws Exception {
        long time = System.currentTimeMillis();
        boolean stop = false;
        int counter=0;
        try (MemoryMappedGenericQueue queue = new MemoryMappedGenericQueue("prime-queue", 2, ResultMessage.class))
        {
            while(!stop)
            {
                queue.offer(new PrimeMessage(random.nextInt(bound)));
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
