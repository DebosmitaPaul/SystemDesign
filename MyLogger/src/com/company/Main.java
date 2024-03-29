package com.company;

public class Main {

    public static void main(String[] args) {
	// write your code here
        LogClient logger = new LoggerClientImp();
        logger.start("1");
        logger.poll();
        logger.start("3");
        logger.poll();
        logger.end("1");
        logger.poll();
        logger.start("2");
        logger.poll();
        logger.end("2");
        logger.poll();
        logger.end("3");
        logger.poll();
        logger.poll();
        logger.poll();
        //1,3,2
        //what about concurrency??
    }
}
