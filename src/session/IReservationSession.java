package session;

import rental.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface IReservationSession extends Remote {
    Set<String> getAllRentalCompanies() throws RemoteException;

    void createQuote(ReservationConstraints constraints, String client) throws Exception;

    void createQuote(Date start, Date end, String carType, String region, String client) throws Exception;

    Set<Quote> getCurrentQuotes() throws RemoteException;

    List<Reservation> confirmQuotes(String name) throws ReservationException, RemoteException;

    Set<CarType> getAvailableCarTypes(Date start, Date end) throws RemoteException;

    String getCheapestCarType(Date start, Date end, String region) throws RemoteException;
}
