package de.carlos.socketfront;

import org.apache.log4j.Logger;

public class Testlog {

    public static void main(String[] args) {
	Logger logger = Logger.getLogger("testlogsger");
	logger.debug("debug");
	logger.error("error");
    }

}
