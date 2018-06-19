package com.ss.random;

import com.ss.prime.queue.messages.MemoryMappedMessage;
import com.ss.queue.*;
import com.ss.prime.queue.messages.PrimeMessage;
import com.ss.prime.queue.messages.ResultMessage;

import java.io.File;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Randomizer {
    private final Random random;
    private final int bound = Integer.MAX_VALUE;
    private final MemoryMappedGenericQueue<PrimeMessage> queue;
    private final MemoryMappedGenericQueue<ResultMessage> resultQueue;
    private final ExecutorService executorService;

    public Randomizer() throws Exception {
        String primeQ = "prime-queue";
        String resultQ = "result-queue";
        deleteFilesOnExit(primeQ, resultQ);
        this.random = new Random();
        queue = new MemoryMappedGenericQueue(primeQ, 2, PrimeMessage.class);
        resultQueue = new MemoryMappedGenericQueue(resultQ, 2, ResultMessage.class);
        executorService = Executors.newSingleThreadExecutor();
    }

    private void deleteFilesOnExit(String primeQ, String resultQ) {
        File fPrime = new File(primeQ);
        File fResult = new File(resultQ);
        fPrime.deleteOnExit();
        fResult.deleteOnExit();
    }

    public static void main(String[] args) throws Exception {
        Randomizer randomizer = new Randomizer();
        randomizer.startReceivingResults();
        randomizer.sendRandom();
    }

    private void startReceivingResults() {
        executorService.execute(getResult());
    }

    private final Runnable getResult() {
        return () -> {
            while(true) {
                MemoryMappedMessage msg = resultQueue.poll();
                if(msg != null) {
                    ResultMessage resultMessage = (ResultMessage)msg;
                    System.out.printf("[%s] %d is %s\n",Thread.currentThread().getName(), resultMessage.getValue(),
                            (resultMessage.isPrime()?"prime":"composite"));
                }

            }
        };
    }

    private void sendRandom() throws Exception {
        long time = System.currentTimeMillis();
        boolean stop = false;
        int counter=0;

        while(!stop)
        {
            int next = random.nextInt(bound);
            queue.offer(new PrimeMessage(next));
            counter++;
            System.out.printf("Sent %d\n", next);
            if(counter == 1000){
                stop = true;
            }
            Thread.sleep(10);
        }
        System.out.printf("Time taken for sending: %d messages was : %d ms\n", counter, (System.currentTimeMillis() - time));
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.MINUTES);
    }
}
