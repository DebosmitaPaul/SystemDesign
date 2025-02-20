package BlockingQueueImp;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class BQMain {
    public static Queue<Integer> queue = new LinkedList<>();
    public static int MAX_SIZE =5;
    public static void main(String[] args) {
         ExecutorService service = Executors.newFixedThreadPool(6);
         for(int i=1; i<=2;i++){
             service.submit(new ProducerThread(i));
         }
        for(int i=1; i<=4;i++){
            service.submit(new ConsumerThread(i));
        }
        service.shutdown();
//        Thread pth = new Thread(new ProducerThread());
//        Thread cth = new Thread(new ConsumerThread());
//        pth.start();
//        cth.start();

    }

    static class ProducerThread implements Runnable {
        int id;
        ProducerThread(int id){
            this.id = id;
        }
        @Override
        public void run() {
            for (int i = 1; i <=5; i++) {
                synchronized (queue) {
                    if (queue.size() >= MAX_SIZE) {
                        System.out.println("Producer in waiting state...");
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    queue.add( (this.id*10) + i);
                    System.out.println(this.id+" ProducerThread " + ((this.id*10) + i));
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    queue.notifyAll();
                }
            }
        }
    }

    static class ConsumerThread implements Runnable {
        int id;
        ConsumerThread(int id){
            this.id = id;
        }
        @Override
        public void run() {
            for (int i = 1; i <=5; i++) {
                synchronized (queue) {
                    if (queue.isEmpty()) {
                        System.out.println("Consumer in waiting state..");
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int x = queue.poll();
                    System.out.println(this.id+" ConsumerThread " + x);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    queue.notifyAll();
                }
            }
        }
    }
}
