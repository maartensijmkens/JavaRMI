package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.List;
import java.util.Set;

import rental.Quote;
import rental.Reservation;
import rental.CarType;
import session.ICarRentalCompany;
import session.IManagerSession;
import session.IRentalServer;
import session.IReservationSession;

public class Client extends AbstractTestManagement<IReservationSession, IManagerSession> {
	
	/********
	 * MAIN *
	 ********/
	
	public static void main(String[] args) throws Exception {
		
		System.setSecurityManager(null);
		
		// An example reservation scenario on car rental company 'Hertz' would be...
		Client client = new Client("trips", IRentalServer.name);
		client.run();
	}
	
	public IRentalServer rentalServer;
	
	/***************
	 * CONSTRUCTOR 
	 * @throws RemoteException 
	 * @throws NotBoundException *
	 ***************/
	
	public Client(String scriptFile, String serverName) throws RemoteException, NotBoundException {
		super(scriptFile);
		Registry registry = LocateRegistry.getRegistry("localhost");
        rentalServer = (IRentalServer) registry.lookup(serverName);
	}


	@Override
	protected Set<String> getBestClients(IManagerSession ms) throws Exception {
		return ms.getBestCustomer();
	}

	@Override
	protected String getCheapestCarType(IReservationSession iReservationSession, Date start, Date end, String region) throws Exception {
		return null;
	}

	@Override
	protected CarType getMostPopularCarTypeIn(IManagerSession ms, String carRentalCompanyName, int year) throws Exception {
		return null;
	}

	@Override
	protected IReservationSession getNewReservationSession(String name) throws Exception {
		return rentalServer.createReservationSession();
	}

	@Override
	protected IManagerSession getNewManagerSession(String name, String carRentalName) throws Exception {
		return rentalServer.createManagerSession();
	}

	@Override
	protected void checkForAvailableCarTypes(IReservationSession reservationSession, Date start, Date end) throws RemoteException {
		Set<CarType> carTypes = reservationSession.getAvailableCarTypes(start, end);
		System.out.print("Available cars: ");
		for (CarType carType : carTypes) {
			System.out.print(carType.getName() + ", ");
		}
		System.out.println();
	}

	@Override
	protected void addQuoteToSession(IReservationSession reservationSession, String name, Date start, Date end, String carType, String region) throws Exception {
		reservationSession.createQuote(start, end, carType, region, name);
	}

	@Override
	protected List<Reservation> confirmQuotes(IReservationSession reservationSession, String name) throws Exception {
		return reservationSession.confirmQuotes(name);
	}

	@Override
	protected int getNumberOfReservationsBy(IManagerSession ms, String clientName) throws Exception {
		return ms.getNumberOfReservationsBy(clientName);
	}

	@Override
	protected int getNumberOfReservationsForCarType(IManagerSession ms, String carRentalName, String carType) throws Exception {
        return ms.getNumberOfReservations(carRentalName).get(carType);
	}
}