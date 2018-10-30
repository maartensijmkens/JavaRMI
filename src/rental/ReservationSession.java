package rental;

import session.IReservationSession;

import java.rmi.RemoteException;
import java.util.*;

public class ReservationSession implements IReservationSession {

    Set<Quote> quotes = new HashSet<>();

    @Override
    public synchronized Set<String> getAllRentalCompanies() throws RemoteException {
        return new HashSet<>(RentalServer.getCompanies().keySet());
    }

    @Override
    public synchronized Set<CarType> getAvailableCarTypes(Date start, Date end) throws RemoteException {
        Set<String> companies = getAllRentalCompanies();
        Set<CarType> carTypes = new HashSet<>();
        for (String company : companies) {
            try {
                carTypes.addAll(RentalServer.getCompany(company).getAvailableCarTypes(start, end));
            } catch(IllegalArgumentException e) {

            }
        }
        return carTypes;
    }

    @Override
    public String getCheapestCarType(Date start, Date end, String region) throws RemoteException {
        Set<String> companies = getAllRentalCompanies();
        String cheapest = null;
        double currentPrice = Double.MAX_VALUE;
        for (String company : companies) {
            if (RentalServer.getCompany(company).hasRegion(region)) {
                Set<CarType> carTypes = RentalServer.getCompany(company).getAvailableCarTypes(start, end);
                for (CarType carType : carTypes) {
                    if (carType.getRentalPricePerDay() < currentPrice) {
                        currentPrice = carType.getRentalPricePerDay();
                        cheapest = carType.getName();
                    }
                }
            }
        }
        return cheapest;
    }

    @Override
    public synchronized void createQuote(ReservationConstraints constraints, String client) throws Exception {
        Set<String> companies = getAllRentalCompanies();
        Exception exception = null;
        for (String company : companies) {
            try {
                Quote quote = RentalServer.getCompany(company).createQuote(constraints, client);
                quotes.add(quote);
                return;
            }
            catch (IllegalArgumentException | ReservationException e) {
                exception = e;
            }
        }
        throw exception;
    }

    @Override
    public void createQuote(Date start, Date end, String carType, String region, String client) throws Exception {
        ReservationConstraints constraints = new ReservationConstraints(start, end, carType, region);
        createQuote(constraints, client);
    }

    @Override
    public synchronized Set<Quote> getCurrentQuotes() throws RemoteException {
        return quotes;
    }

    @Override
    public synchronized List<Reservation> confirmQuotes(String name) throws ReservationException, RemoteException {
        List<Reservation> reservations = new ArrayList<Reservation>();
        for (Quote quote : this.quotes) {
            if (quote.getCarRenter().equals(name)) {
                try {
                    String company = quote.getRentalCompany();
                    Reservation reservation = RentalServer.getCompany(company).confirmQuote(quote);
                    reservations.add(reservation);
                }
                catch (ReservationException e) {
                    for (Reservation reservation : reservations) {
                        String company = reservation.getRentalCompany();
                        RentalServer.getCompany(company).cancelReservation(reservation);
                    }
                    throw e;
                }
            }
        }
        return reservations;
    }
}
