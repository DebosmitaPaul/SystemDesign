package com.company;

public class Process {
    private final String processId;
    private final long startTime;
    private long endTime;

    public Process(final String processId,final long startTime) {
        this.processId = processId;
        this.startTime = startTime;
        endTime = -1;
    }
    public String getProcessId(){
        return  this.processId;
    }
    public long getStartTime(){
        return startTime;
    }

    public long getEndTime(){
        return endTime;
    }
    public void setEndTime(long time){
        this.endTime = time;
    }
}
