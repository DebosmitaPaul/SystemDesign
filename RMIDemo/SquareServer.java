package com.RMIDemo;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class SquareServer {

    public static void main(String[] args) {
        try {
            SquareService service = new SquareServiceImpl();
            Registry registry = LocateRegistry.createRegistry(1099); // Default RMI port
            registry.bind("SquareService", service);

            AdditionService addService = new AdditionServiceImpl();
            registry.bind("AdditionService", addService);
            System.out.println("SquareService ready.");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}