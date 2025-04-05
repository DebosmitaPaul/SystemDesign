package com.OwnSSEProtocol;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class CustomSSEClient {
    public static void main(String[] args) {
        try{
            Socket socket = new Socket("localhost",9020);
            InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
            BufferedReader reader = new BufferedReader(inputStreamReader);
            System.out.println("client...");
//    event: <event_name>\ndata: <data>\n\n
            StringBuilder msgBuilder = new StringBuilder();
            String line;// = reader.readLine() ;
            StringBuilder msg = new StringBuilder();
            while((line = reader.readLine()) != null){
//                System.out.println("line ..." + line);
                msgBuilder.append(line).append("\n");
                if(!line.isEmpty()){
                    msg = msg.append(msgBuilder.toString());
                    msgBuilder.setLength(0);
                }else{
                    parseMsg(msg.toString());
                    msg= new StringBuilder();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    static void parseMsg(String msg){
//    event: <event_name>\ndata: <data>\n\n
        String event = null;
        String data = null;
        String[] lines = msg.split("\n");
        for(String line: lines){
            if(line.startsWith("event: ")){
                event = line.substring(7);
            }else if(line.startsWith("data: ")){
                data = line.substring(6);
            }
        }
        if(event!=null && data!=null){
            System.out.println("event: "+event+"\ndata: "+data+"\n\n");
            if(event=="close"){
                System.out.println("Connection closed by server..");
                System.exit(0);
            }
        }
    }
}
