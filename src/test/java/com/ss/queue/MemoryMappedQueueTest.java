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

public class MemoryMappedQueueTest
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

        try (MemoryMappedQueue queue = new MemoryMappedQueue(filePath, 2))
        {
            queue.offer(10);
            while (true) {
                Integer result = queue.poll();
                if (result != null) {
                    Assert.assertSame(10, result);
                    break;
                }
            }
        }
    }

    @Test(timeout = 1000L)
    public void testMultipleOfferAndPoll() throws Exception {
        try (MemoryMappedQueue queue = new MemoryMappedQueue(filePath, 2)) {
            List<Integer> inputs = Arrays.asList(10, -1, 0, 2, Integer.MIN_VALUE, 200, Integer.MAX_VALUE);
            inputs.stream().forEach(queue::offer);
            pollAndAssertResults(queue, inputs);
        }
    }

    @Test(timeout = 1000L)
    public void testOfferAndPollRandom() throws Exception {

        try (MemoryMappedQueue queue = new MemoryMappedQueue(filePath, 2))
        {
            Random random = new Random();
            List<Integer> inputs = getRandomInputs(random);
            inputs.stream().forEach(queue::offer);
            pollAndAssertResults(queue, inputs);
        }
    }

    private List<Integer> getRandomInputs(Random random) {
        return Stream.generate(() -> random.nextInt(Integer.MAX_VALUE)).limit(1_000).collect(toList());
    }

    private void pollAndAssertResults(MemoryMappedQueue queue, List<Integer> inputs) {
        List<Integer> results = new ArrayList<>();
        do {
            Integer result = queue.poll();
            if(result != null) {
                results.add(result);
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
