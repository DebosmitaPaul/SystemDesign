package com.RMIDemo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AdditionServiceImpl extends UnicastRemoteObject implements AdditionService {

    public AdditionServiceImpl() throws RemoteException{
        super();
    }
    @Override
    public int add(int number1, int number2) throws RemoteException {
        return number1+number2;
    }
}
