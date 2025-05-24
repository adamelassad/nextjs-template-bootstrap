package covoitu;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RideManager {
    private boolean isConnected;

    public RideManager() {
        try {
            DatabaseManager.connect();
            isConnected = true;
        } catch (SQLException e) {
            e.printStackTrace();
            isConnected = false;
        }
    }

    public String createRide(String driverName, String origin, String destination, String date, int totalSeats) {
        if (!isConnected) return null;

        try {
            String rideId = UUID.randomUUID().toString();
            Ride ride = new Ride(driverName, origin, destination, date, totalSeats);
            ride.setRideId(rideId);
            ride.setStatus("SCHEDULED");
            DatabaseManager.saveRide(ride);
            return rideId;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addRide(Ride ride) {
        if (!isConnected) return;

        try {
            if (ride.getRideId() == null) {
                ride.setRideId(UUID.randomUUID().toString());
            }
            if (ride.getStatus() == null) {
                ride.setStatus("SCHEDULED");
            }
            DatabaseManager.saveRide(ride);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Ride> getRides() {
        if (!isConnected) return new ArrayList<>();

        try {
            return DatabaseManager.searchRides("", ""); // Empty strings to match all rides
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Ride> getAvailableRides() {
        List<Ride> availableRides = new ArrayList<>();
        for (Ride ride : getRides()) {
            if ("SCHEDULED".equalsIgnoreCase(ride.getStatus()) && ride.getAvailableSeats() > 0) {
                availableRides.add(ride);
            }
        }
        return availableRides;
    }

    public List<Ride> searchRides(String origin, String destination) {
        if (!isConnected) return new ArrayList<>();

        try {
            return DatabaseManager.searchRides(origin, destination);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean updateRideStatus(String rideId, String status) {
        if (!isConnected) return false;

        try {
            DatabaseManager.updateRideStatus(rideId, status);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateAvailableSeats(String rideId, int availableSeats) {
        if (!isConnected) return false;

        try {
            DatabaseManager.updateRideAvailableSeats(rideId, availableSeats);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Ride getRide(String rideId) {
        if (!isConnected) return null;

        try {
            return DatabaseManager.getRide(rideId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean deleteRide(String rideId) {
        if (!isConnected) return false;

        try {
            DatabaseManager.deleteRide(rideId);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
