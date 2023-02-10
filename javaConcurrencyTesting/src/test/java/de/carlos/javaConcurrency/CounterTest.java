package de.carlos.javaConcurrency;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class CounterTest {

    Logger logger = LoggerFactory.getLogger(CounterTest.class);

    @Test
    public void justRun() {
        Counter counter = new Counter();
        counter.run();
        assertTrue(counter.isDone());
    }

    @Test
    public void withExecutor() throws InterruptedException {

        List<Counter> counters = Arrays.asList(new Counter(), new Counter(), new Counter());

        ExecutorService executor = Executors.newFixedThreadPool(2);
        counters.forEach(executor::execute);
        executor.shutdown();
        assertTrue(executor.awaitTermination(3, TimeUnit.SECONDS));
        assertTrue(counters.stream().allMatch(counter -> counter.isDone()));
    }

    @Test
    public void cancel() throws InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Counter counter = new Counter();
        Future future = executorService.submit(counter);

        Thread.sleep(250);
        future.cancel(true);
        assertEquals(2, counter.getValue());
    }

    @Test
    public void futures() {
        List<Counter> counters = Arrays.asList(new Counter(), new Counter(), new Counter());

        ExecutorService executor = Executors.newSingleThreadExecutor();
        List<Future<Counter>> futures = counters.stream()
                .map(counter -> executor.submit(counter, counter))
                .toList();

        //Wait for first two
        futures.subList(0, 2).stream().forEach(future -> {
            try {
                Counter counter = future.get(2, TimeUnit.SECONDS);
                logger.debug("{} finished", counter);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        //Last one should not yet be done.
        assertFalse(futures.get(2).isDone());

    }

}