import java.util.Date;

public class UserProfile {
    private String userId;
    private String username;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String address;
    private String phoneNumber;
    private String email;
    private double rating;
    private int totalRides;
    private String userType; // "DRIVER" or "PASSENGER" or "ADMIN"
    private Date registrationDate;

    public UserProfile(String userId, String username, String firstName, String lastName) {
        this.userId = userId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.rating = 5.0; // Default rating
        this.totalRides = 0;
        this.registrationDate = new Date();
        this.userType = "PASSENGER"; // Default type
    }

    // Username getter and setter
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public Date getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public int getTotalRides() { return totalRides; }
    public void setTotalRides(int totalRides) { this.totalRides = totalRides; }

    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }

    public Date getRegistrationDate() { return registrationDate; }

    // Methods for profile management
    public void updateRating(double newRating) {
        // Update rating based on new review
        this.rating = (this.rating * this.totalRides + newRating) / (this.totalRides + 1);
        this.totalRides++;
    }

    public boolean isDriver() {
        return userType.equals("DRIVER");
    }

    public boolean isAdmin() {
        return userType.equals("ADMIN");
    }

    @Override
    public String toString() {
        return String.format("%s %s (%s)", firstName, lastName, userType);
    }
}
