package com.company;

import java.util.Comparator;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LoggerClientImp implements LogClient{
    private final Map<String,Process> processMap; // (ProcessId,Process)
    // required sorted map to store earliest time first.
    // not HashMap/Heap -> O(N) for sorting. Heapify also O(N)
    private final ConcurrentSkipListMap<Long,Process> queue;
    private final List<CompletableFuture<Void>> futures;
    private Lock lock;
    private ExecutorService[] taskScheduler;

    public LoggerClientImp() {
        this.processMap = new ConcurrentHashMap<>();//new HashMap<>();
        this.queue = new ConcurrentSkipListMap<>();
                //new TreeMap<>(Comparator.comparingLong(start -> start)); //sorted with smallest start time;
        futures = new CopyOnWriteArrayList<>(); //CopyOnWriteArrayList is concurrent for list
        this.lock =new ReentrantLock();
        this.taskScheduler = new ExecutorService[10];
        for (int i = 0; i < taskScheduler.length; i++) {
            taskScheduler[i] = Executors.newSingleThreadExecutor();
        }
    }
    // start(), end(), poll() can call concurrently.
    // end() call -> no data in processMap throw nullpointerException -> resulting no end() method being acknowladged
    // -> Start() will create a memory leak
    @Override
    public void start(String processId) { //T(O)=logN , create
        taskScheduler[processId.hashCode() % taskScheduler.length].execute(()-> {
            final long timeNow = System.currentTimeMillis();
            final Process process = new Process(processId, timeNow);
            // when starting any event publishing to the map and queue.
            processMap.put(processId, process);
            queue.put(timeNow, process);
        });
    }

    @Override
    public void end(String processId) { //T(0) = 1 , Update
        taskScheduler[processId.hashCode() % taskScheduler.length].execute(()-> {// assigning same process into a same thread
            lock.lock();
            try {
                final long now = System.currentTimeMillis();
                // publish to map
                processMap.get(processId).setEndTime(now);
                // not publish to queue.
                if (!futures.isEmpty() && queue.firstEntry().getValue().getEndTime() != -1) {
                    pollNow();
                    var result = futures.remove(0);
                    result.complete(null);
                }
            } finally {
                lock.unlock();
                ;
            }
        });
    }
//poll and end() can't run together so we use lock
    @Override
    public String poll() { //T(O) = LogN , delete and read
        lock.lock();
        final var result = new CompletableFuture<Void>();
        if(!queue.isEmpty() &&   queue.firstEntry().getValue().getEndTime() != -1){
           pollNow();
        }else{
            //wait for end of first process id
            futures.add(result);
        }
        try {
             result.get(3, TimeUnit.SECONDS); // blocking method
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
        finally {
            lock.unlock();
        }
        return null;
    }

    private String pollNow() {
        Process process = queue.firstEntry().getValue();
        var logstatement = process.getProcessId()+" started at "+process.getStartTime()+" and ended at "+process.getEndTime();
        System.out.println(logstatement);
         //delete the processId which has smallest start time (started earliest).
        processMap.remove(process.getProcessId());
        queue.pollFirstEntry();
        return logstatement;
    }
}
