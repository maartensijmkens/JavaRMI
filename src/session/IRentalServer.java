package session;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRentalServer extends Remote {

    String name = "server";

    IReservationSession createReservationSession() throws RemoteException, NotBoundException;

    IManagerSession createManagerSession() throws RemoteException, NotBoundException;

}