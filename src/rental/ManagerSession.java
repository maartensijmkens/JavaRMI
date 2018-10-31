package rental;

import session.IManagerSession;

import java.rmi.RemoteException;
import java.util.*;

public class ManagerSession implements IManagerSession {

    @Override
    public Set<String> getAllRentalCompanies() throws RemoteException {
        return new HashSet<>(RentalServer.getCompanies().keySet());
    }

    @Override
    public Collection<CarType> getCarTypes(String company) throws RemoteException {
        return RentalServer.getCompany(company).getAllCarTypes();
    }

    @Override
    public int getNumberOfReservations(String company, String carType) throws RemoteException {
        return RentalServer.getCompany(company).getNumberOfReservationForCarType(carType);
    }

    @Override
    public int getNumberOfReservationsBy(String client) throws RemoteException {
        int total = 0;
        Set<String> companies = getAllRentalCompanies();
        for (String company : companies) {
            total += RentalServer.getCompany(company).getReservationsByRenter(client).size();
        }
        return total;
    }


    @Override
    public Set<String> getBestCustomer() throws RemoteException {
        Set<String> companies = getAllRentalCompanies();
        Set<String> clients = new HashSet<String>();
        int best = 0;
        Set<String> bestClients = new HashSet<>();
        for (String company : companies) {
            clients.addAll(RentalServer.getCompany(company).getClients());
        }
        for (String client : clients) {
            int current = 0;
            for (String company : companies) {
                current += RentalServer.getCompany(company).getReservationsByRenter(client).size();
            }
            if (current == best) {
                bestClients.add(client);
            }
            if (current > best) {
                best = current;
                bestClients.clear();
                bestClients.add(client);
            }
        }
        return bestClients;
    }

    @Override
    public CarType getMostPopularCarTypeIn(String company, int year) throws RemoteException {
         Set<CarType> carTypes = RentalServer.getCompany(company).getAllCarTypes();
         CarType mostPopular = null;
         int most = 0;
         for (CarType carType : carTypes) {
             int current = RentalServer.getCompany(company).getNumberOfReservationForCarType(carType.getName());
             if (current > most) {
                 most = current;
                 mostPopular = carType;
             }
         }
         return mostPopular;
    }
}
