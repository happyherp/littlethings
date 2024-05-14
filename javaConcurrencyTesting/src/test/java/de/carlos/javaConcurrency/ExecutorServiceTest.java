package de.carlos.javaConcurrency;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExecutorServiceTest {

    private Logger logger = LoggerFactory.getLogger(ExecutorServiceTest.class);

    @Test
    public void awaitTermination() throws InterruptedException {
        ExecutorService executorService =  Executors.newSingleThreadExecutor();
        executorService.submit(() -> logger.info("I RAN!"));
        executorService.shutdown();
        boolean terminated = executorService.awaitTermination(1, TimeUnit.SECONDS);
        logger.info("After awaitTermination");
        assertTrue(terminated);
    }

    @Test
    public void future() throws ExecutionException, InterruptedException {
        ExecutorService executorService =  Executors.newSingleThreadExecutor();
        Future<String> future =  executorService.submit(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "I am done";
        });
        logger.debug("Submitted future");
        String result = future.get();
        logger.debug("Got result {}", result);
        assertEquals("I am done", result);

    }

}
