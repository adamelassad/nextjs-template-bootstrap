public class Ride {
    private String driverName;
    private String origin;
    private String destination;
    private String date;
    private int availableSeats;

    public Ride(String driverName, String origin, String destination, String date, int availableSeats) {
        this.driverName = driverName;
        this.origin = origin;
        this.destination = destination;
        this.date = date;
        this.availableSeats = availableSeats;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getDate() {
        return date;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }
}
