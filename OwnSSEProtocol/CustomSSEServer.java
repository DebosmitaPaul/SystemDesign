package com.OwnSSEProtocol;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomSSEServer {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        try(ServerSocket serverSocket = new ServerSocket(9020)){
            serverSocket.setReuseAddress(true);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                int port = clientSocket.getPort();
                System.out.println("listing port.. " + port);
                executorService.submit(() -> handleClient(clientSocket));
            }
        }catch (Exception ex){
            System.err.println("an error occoured..");
            ex.printStackTrace();
        }finally {
            executorService.shutdown();
        }
    }
    static void handleClient(Socket clientSocket){
        try{
            OutputStream outputStream = clientSocket.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            BufferedWriter writer = new BufferedWriter(outputStreamWriter);
            System.out.println("Client connected on port.. "+ clientSocket.getPort());
//    event: <event_name>\ndata: <data>\n\n
            String eventName = "message";
            for (int i = 0; i < 10; i++) {
                String data = "Data"+i;
                String msg = "event: "+eventName+"\ndata: "+data+"\n\n";
                System.out.println(msg);
                writer.write(msg);
                writer.flush();
                Thread.sleep(1000);
            }
            writer.write("event: close\ndata: connection closed\n\n");
            writer.flush();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
