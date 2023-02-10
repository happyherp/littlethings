package de.carlos.javaConcurrency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Counter implements Runnable{

    Logger logger = LoggerFactory.getLogger(Counter.class);


    private int value = 0;
    private int countTo = 10;
    private long sleepMs = 100;

    @Override
    public void run() {
        logger.debug("Starting {}", this);

        while (value<countTo){

            try {
                Thread.sleep(sleepMs);
            } catch (InterruptedException e) {
                logger.debug("I was interrupted!", e);
                throw new RuntimeException(e);
            }
            value++;
            logger.debug("Counted to {}", value);
        }

        logger.debug("End");
    }

    public boolean isDone(){
        return value == countTo;
    }

    public int getValue() {
        return value;
    }
}
