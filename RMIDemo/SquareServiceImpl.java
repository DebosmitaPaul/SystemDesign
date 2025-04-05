package com.RMIDemo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class SquareServiceImpl extends UnicastRemoteObject implements SquareService {

    public SquareServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public int square(int number) throws RemoteException {
        return number * number;
    }
}