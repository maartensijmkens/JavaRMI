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
    public Map<String,Integer> getNumberOfReservations(String company) throws RemoteException {
        Map<String,Integer> map = new HashMap<>();
        List<Car> cars = RentalServer.getCompany(company).getCars();
        for (Car car : cars) {
            String carType = car.getType().getName();
            int nb = car.getAllReservations().size();
            if (map.containsKey(carType)) {
                map.put(carType, map.get(carType)+nb);
            }
            else {
                map.put(carType, nb);
            }
        }
        return map;
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
}
