package com.company;

public interface LogClient {
    void start(String processId);
    void end(String processId);
    /*
    * Polls the first log entry of a completeed process sorted by the start time in the below format.
    * {processId} started at {startTime} and ended at {endTime}
    *
    * process id {3} started at {7} and ended at {19}
    * process id {2} started at {8} and ended at {12}
    * process id {1} started at {12} and ended at {15}
    */
    String poll();
}
