package com.RMIDemo;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class SquareClient {

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099); // Connect to registry
            SquareService squareService = (SquareService) registry.lookup("SquareService");
            int result = squareService.square(5); // Remote method invocation
            System.out.println("Square of 5: " + result);

            AdditionService addService = (AdditionService) registry.lookup("AdditionService");
            result = addService.add(10 , 12);
            System.out.println("Addition is "+ result);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}