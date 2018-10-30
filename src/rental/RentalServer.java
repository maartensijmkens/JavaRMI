package rental;

import session.ICarRentalCompany;
import session.IManagerSession;
import session.IRentalServer;
import session.IReservationSession;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class RentalServer implements IRentalServer {

    static Map<String, ICarRentalCompany> companies = new HashMap<>();

	public static void main(String[] args) throws ReservationException,
            NumberFormatException, IOException, AlreadyBoundException, NotBoundException {
		System.setSecurityManager(null);

        RentalServer rentalServer = new RentalServer();
        IRentalServer serverStub = (IRentalServer) UnicastRemoteObject.exportObject(rentalServer, 0);
        LocateRegistry.getRegistry().bind(IRentalServer.name,serverStub);

		String[] companies = {"hertz", "dockx"};
		for (String name : companies) {
            CrcData data = loadData(name + ".csv");
            CarRentalCompany company = new CarRentalCompany(data.name, data.regions, data.cars);
            ICarRentalCompany companyStub = (ICarRentalCompany) UnicastRemoteObject.exportObject(company, 0);
            LocateRegistry.getRegistry().bind(data.name, companyStub);
            rentalServer.registerCompany(data.name);
        }


	}

	@Override
    public IReservationSession createReservationSession() throws RemoteException, NotBoundException {
        return (IReservationSession) UnicastRemoteObject.exportObject(new ReservationSession(), 0);
    }

    @Override
    public IManagerSession createManagerSession() throws RemoteException, NotBoundException {
        return (IManagerSession) UnicastRemoteObject.exportObject(new ManagerSession(), 0);
    }

    public static synchronized Map<String, ICarRentalCompany> getCompanies() {
	    return companies;
    }

    public static synchronized ICarRentalCompany getCompany(String name) {
	    if (companies.containsKey(name))
            return companies.get(name);
        throw new IllegalArgumentException("Company not registered: " + name);
    }

	public static void registerCompany(String name) throws RemoteException, NotBoundException {
        companies.put(name, (ICarRentalCompany) LocateRegistry.getRegistry().lookup(name));
    }


    public static void unregisterCompany(String name) {
	    companies.remove(name);
    }

	public static CrcData loadData(String datafile)
			throws ReservationException, NumberFormatException, IOException {

		CrcData out = new CrcData();
		int nextuid = 0;

		// open file
		BufferedReader in = new BufferedReader(new FileReader(datafile));
		StringTokenizer csvReader;
		
		try {
			// while next line exists
			while (in.ready()) {
				String line = in.readLine();
				
				if (line.startsWith("#")) {
					// comment -> skip					
				} else if (line.startsWith("-")) {
					csvReader = new StringTokenizer(line.substring(1), ",");
					out.name = csvReader.nextToken();
					out.regions = Arrays.asList(csvReader.nextToken().split(":"));
				} else {
					// tokenize on ,
					csvReader = new StringTokenizer(line, ",");
					// create new car type from first 5 fields
					CarType type = new CarType(csvReader.nextToken(),
							Integer.parseInt(csvReader.nextToken()),
							Float.parseFloat(csvReader.nextToken()),
							Double.parseDouble(csvReader.nextToken()),
							Boolean.parseBoolean(csvReader.nextToken()));
					System.out.println(type);
					// create N new cars with given type, where N is the 5th field
					for (int i = Integer.parseInt(csvReader.nextToken()); i > 0; i--) {
						out.cars.add(new Car(nextuid++, type));
					}
				}
			}
		} finally {
			in.close();
		}

		return out;
	}
	
	static class CrcData {
		public List<Car> cars = new LinkedList<Car>();
		public String name;
		public List<String> regions =  new LinkedList<String>();
	}

}
