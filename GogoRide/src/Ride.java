package covoitu;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Ride {
    private String rideId;
    private String driverName;
    private String origin;
    private String destination;
    private Date date;
    private int totalSeats;
    private int availableSeats;
    private String status;
    private Set<String> passengers;

    public Ride(String driverName, String origin, String destination, String date, int totalSeats) {
        this.driverName = driverName;
        this.origin = origin;
        this.destination = destination;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
        this.status = "SCHEDULED";
        this.passengers = new HashSet<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            this.date = dateFormat.parse(date);
        } catch (ParseException e) {
            this.date = new Date(); // Default to current date if parsing fails
        }
    }

    // Getters and setters omitted for brevity (same as before)

    public boolean addPassenger(String userId) {
        if (availableSeats > 0 && passengers.add(userId)) {
            availableSeats--;
            return true;
        }
        return false;
    }

    public boolean removePassenger(String userId) {
        if (passengers.remove(userId)) {
            availableSeats++;
            return true;
        }
        return false;
    }

    // Other methods (toString, bookSeat, cancelBooking) omitted for brevity
}
