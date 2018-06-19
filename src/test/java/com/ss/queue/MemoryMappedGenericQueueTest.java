package com.ss.queue;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class MemoryMappedGenericQueueTest
{
    private File file;
    private static final String filePath = "./test-unit-message";

    @Before
    public void setup() throws Exception
    {
        file = new File(filePath);
        file.delete();
    }

    @Test(timeout = 1000L)
    public void testOfferAndPoll() throws Exception {

        try (MemoryMappedGenericQueue<PrimeMessage> queue = new MemoryMappedGenericQueue<>(filePath,
                                                                                            2,
                                                                                            PrimeMessage.class))
        {
            queue.offer(new PrimeMessage(10));
            while (true) {
                MemoryMappedMessage result = queue.poll();
                if (result != null) {
                    Assert.assertSame(10, result.getValue());
                    break;
                }
            }
        }
    }

    @Test(timeout = 1000L)
    public void testOfferAndPollResultMsg() throws Exception {

        try (MemoryMappedGenericQueue<ResultMessage> queue = new MemoryMappedGenericQueue<>(filePath,
                2,
                ResultMessage.class))
        {
            queue.offer(new ResultMessage(10, (byte) 0));
            while (true) {
                ResultMessage result = (ResultMessage)queue.poll();
                if (result != null) {
                    Assert.assertSame(10, result.getValue());
                    Assert.assertFalse(result.isPrime());
                    break;
                }
            }
        }
    }

    @Test(timeout = 2000L)
    public void testMultipleOfferAndPoll() throws Exception {
        try (MemoryMappedGenericQueue queue = new MemoryMappedGenericQueue(filePath, 2,PrimeMessage.class)) {
            List<Integer> inputs = Arrays.asList(10, -1, 0, 2, Integer.MIN_VALUE, 200, Integer.MAX_VALUE);
            inputs.stream().map(i -> new PrimeMessage(i)).forEach(queue::offer);
            pollAndAssertResults(queue, inputs);
        }
    }

    @Test(timeout = 2000L)
    public void testOfferAndPollRandom() throws Exception {

        try (MemoryMappedGenericQueue queue = new MemoryMappedGenericQueue(filePath, 2,PrimeMessage.class))
        {
            Random random = new Random();
            List<Integer> inputs = getRandomInputs(random);
            inputs.stream().map(i -> new PrimeMessage(i)).forEach(queue::offer);
            pollAndAssertResults(queue, inputs);
        }
    }

    private List<Integer> getRandomInputs(Random random) {
        return Stream.generate(() -> random.nextInt(Integer.MAX_VALUE)).limit(1_000).collect(toList());
    }

    private void pollAndAssertResults(MemoryMappedGenericQueue queue, List<Integer> inputs) {
        List<Integer> results = new ArrayList<>();
        do {
            MemoryMappedMessage result = queue.poll();
            if(result != null) {
                results.add(result.getValue());
            }
        }while(results.size() < inputs.size());
        Assert.assertEquals(inputs, results);
    }

    @After
    public void tearDown()
    {
        file.delete();
    }
}
