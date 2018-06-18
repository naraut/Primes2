package com.ss.prime;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class PrimeTest {

    @Test(timeout=1000L)
    public void testSingle() throws Exception {
        Primes primes = new Primes("test-unit1");
        long time = System.currentTimeMillis();
        //632658701 is composite
        int candidate =  8501*19309;//525702829;//632658701;//composite
        boolean isPrime = primes.testPrime(candidate);
//        System.out.printf("%d is %s\n", candidate, (isPrime?"prime":"composite"));
        Assert.assertFalse(isPrime);
        System.out.printf("Time taken: %d\n" ,(System.currentTimeMillis()-time)/4);
    }

    @Test(timeout=1000L)
    public void testSmall() throws Exception {
        Primes primes = new Primes("test-unit1");
        long time = System.currentTimeMillis();
        //632658701 is composite
        int[] candidates = new int[]{ 633910111,633910117,633910163,633910177,633910181,633910183,633910187,633910201,633910241,633910261};
        for (int candidate: candidates)
        {
            boolean isPrime = primes.testPrime(candidate);
            System.out.printf("%d is %s\n", candidate, (isPrime?"prime":"composite"));
            Assert.assertTrue(isPrime);

            isPrime = primes.testPrime(candidate+3);
//            System.out.printf("%d is %s\n", candidate+3, (isPrime?"prime":"composite"));
            Assert.assertFalse(isPrime);
        }
        System.out.printf("Time taken: %d\n" ,(System.currentTimeMillis()-time)/4);
    }

    @Test(timeout=1000L)
    public void testLargeNumbers() throws Exception {
        Primes primes = new Primes("test-unit1");
        int candidate= Integer.MAX_VALUE;
        long time = System.currentTimeMillis();
        for(int i=0;i<100;i++)
        {
            boolean isPrime = primes.testPrime(--candidate);
            System.out.printf("%d is %s\n", candidate, (isPrime?"prime":"composite"));
        }
        System.out.printf("Time taken %d \n", (System.currentTimeMillis() - time));
    }
}
