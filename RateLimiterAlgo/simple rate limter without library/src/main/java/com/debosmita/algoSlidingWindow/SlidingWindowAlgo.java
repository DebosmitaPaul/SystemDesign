package com.debosmita.algoSlidingWindow;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SlidingWindowAlgo {
     Queue<Long> slidingQueue;
     int bucketCapacity;
     int timeWindowInSec;
    public SlidingWindowAlgo(int bucketCapacity, int timeWindowInSec){
        this.bucketCapacity = bucketCapacity; //5
        this.timeWindowInSec = timeWindowInSec; //10sec
        slidingQueue = new ConcurrentLinkedQueue<Long>();
        slidingQueue.add(System.currentTimeMillis());
    }
    public synchronized boolean canConsumeRequest(){
        long currentTime=System.currentTimeMillis();
        validateUpdateQueue(currentTime);
        if(slidingQueue.size() < bucketCapacity){
            //can add req in a bucket/queue
            slidingQueue.offer( currentTime);
            return true;
        }
        return false;
    }

    private void validateUpdateQueue(long currentTime) {
        if(slidingQueue.isEmpty()){
            return;
        }
        Long calculatedTime = currentTime - slidingQueue.peek();//ms
        while( calculatedTime/1000 >= timeWindowInSec ){
            slidingQueue.poll();
            if (slidingQueue.isEmpty()) {
                break;
            }
            calculatedTime = currentTime-slidingQueue.peek();
        }

    }

}
