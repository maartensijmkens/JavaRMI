package session;

import rental.CarType;

import java.rmi.NotBoundException;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Set;

public interface IManagerSession extends Remote {
    Set<String> getAllRentalCompanies() throws RemoteException;

    Collection<CarType> getCarTypes(String company) throws RemoteException;

    int getNumberOfReservations(String company, String carType) throws RemoteException;

    int getNumberOfReservationsBy(String client) throws RemoteException;

    Set<String> getBestCustomer() throws RemoteException;

    CarType getMostPopularCarTypeIn(String company, int year) throws RemoteException;

    void registerCompany(String name) throws RemoteException, NotBoundException;

    void unregisterCompany(String name) throws RemoteException, NoSuchObjectException;

}
