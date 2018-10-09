package rental;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface ICarRentalCompany extends Remote {
	Set<CarType> getAvailableCarTypes(Date start, Date end) throws RemoteException;
	
	Quote createQuote(String clientName, Date start, Date end, String carType, String region)
			throws ReservationException, RemoteException;
	
	Reservation confirmQuote(Quote quote) throws ReservationException, RemoteException;
	
	List<Reservation>getReservationsByRenter(String clientName) throws RemoteException;

	int getNumberOfReservationForCarType(String carType) throws RemoteException;
}