package com.RMIDemo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SquareService extends Remote {
    int square(int number) throws RemoteException;
}