package com.debosmita.algoSlidingWindow;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainSlidingWindow {
    public static void main(String[] args) {
        UserBucketCreator user = new UserBucketCreator(3);
        int poolSize = 12;
        ExecutorService executorService = Executors.newFixedThreadPool(poolSize);
        for(int i=1;i<=poolSize;i++){
            executorService.execute(() -> user.access(3));
        }
        executorService.shutdown();
    }
}
class UserBucketCreator{
    Map<Integer, SlidingWindowAlgo> bucket;
    UserBucketCreator(int id){
        bucket = new HashMap<>();
        bucket.put(id, new SlidingWindowAlgo(5,10));
    }
    void access(int id){
        if(bucket.get(id).canConsumeRequest()){
            System.out.println(Thread.currentThread().getName()+"-- able to access");
        }
        else{
            System.out.println(Thread.currentThread().getName()+"-- NOT able to access");
        }
    }
}
