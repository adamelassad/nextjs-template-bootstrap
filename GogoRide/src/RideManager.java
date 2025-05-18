import java.util.ArrayList;
import java.util.List;

public class RideManager {
    private List<Ride> rides;

    public RideManager() {
        rides = new ArrayList<>();
    }

    public void addRide(Ride ride) {
        rides.add(ride);
    }

    public List<Ride> searchRides(String origin, String destination) {
        List<Ride> results = new ArrayList<>();
        for (Ride ride : rides) {
            if (ride.getOrigin().equalsIgnoreCase(origin) && ride.getDestination().equalsIgnoreCase(destination)) {
                results.add(ride);
            }
        }
        return results;
    }

    public List<Ride> getAllRides() {
        return rides;
    }
}
