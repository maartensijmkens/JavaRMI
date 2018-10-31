package session;

import rental.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface ICarRentalCompany extends Remote {
	Set<CarType> getAvailableCarTypes(Date start, Date end) throws RemoteException;
	
	Quote createQuote(String clientName, Date start, Date end, String carType, String region)
			throws ReservationException, RemoteException;

	Quote createQuote(ReservationConstraints constraints, String client) throws RemoteException, ReservationException;
	
	Reservation confirmQuote(Quote quote) throws ReservationException, RemoteException;

	void cancelReservation(Reservation res) throws RemoteException;
	
	List<Reservation>getReservationsByRenter(String clientName) throws RemoteException;

	int getNumberOfReservationForCarType(String carType) throws RemoteException;

    Set<CarType> getAllCarTypes() throws RemoteException;

    List<Car> getCars() throws RemoteException;

    Set<String> getClients() throws RemoteException;

	boolean hasRegion(String region) throws RemoteException;
}