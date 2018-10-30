package session;

import rental.CarType;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface IManagerSession extends Remote {
    Set<String> getAllRentalCompanies() throws RemoteException;

    Collection<CarType> getCarTypes(String company) throws RemoteException;

    Map<String, Integer> getNumberOfReservations(String company) throws RemoteException;

    int getNumberOfReservationsBy(String client) throws RemoteException;

    Set<String> getBestCustomer() throws RemoteException;
}