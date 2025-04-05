package com.RMIDemo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AdditionService extends Remote {
    int add(int number1, int number2) throws RemoteException;
}
