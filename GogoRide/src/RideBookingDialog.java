import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class RideBookingDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    private RideManager rideManager;
    private UserProfile currentUser;
    private Ride selectedRide;
    private JList<Ride> rideList;
    private DefaultListModel<Ride> rideListModel;
    private JTextArea rideDetailsArea;
    private SimpleDateFormat dateFormat;

    public RideBookingDialog(Frame parent, UserProfile currentUser, RideManager rideManager) {
        super(parent, "Book a Ride", true);
        this.currentUser = currentUser;
        this.rideManager = rideManager;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        setSize(800, 500);
        setLocationRelativeTo(parent);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Left panel with available rides
        rideListModel = new DefaultListModel<>();
        loadAvailableRides();
        rideList = new JList<>(rideListModel);
        rideList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        rideList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectedRide = rideList.getSelectedValue();
                updateRideDetails();
            }
        });

        JScrollPane rideScrollPane = new JScrollPane(rideList);
        rideScrollPane.setPreferredSize(new Dimension(300, 0));
        add(rideScrollPane, BorderLayout.WEST);

        // Right panel with ride details and booking options
        JPanel rightPanel = new JPanel(new BorderLayout());

        // Ride details area
        rideDetailsArea = new JTextArea();
        rideDetailsArea.setEditable(false);
        rideDetailsArea.setLineWrap(true);
        rideDetailsArea.setWrapStyleWord(true);
        JScrollPane detailsScrollPane = new JScrollPane(rideDetailsArea);
        rightPanel.add(detailsScrollPane, BorderLayout.CENTER);

        // Booking panel
        JPanel bookingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton bookButton = new JButton("Book Ride");
        bookButton.addActionListener(e -> bookRide());
        
        JButton cancelButton = new JButton("Cancel Booking");
        cancelButton.addActionListener(e -> cancelBooking());

        bookingPanel.add(bookButton);
        bookingPanel.add(cancelButton);
        rightPanel.add(bookingPanel, BorderLayout.SOUTH);

        add(rightPanel, BorderLayout.CENTER);

        // Top panel with filters
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField originFilter = new JTextField(15);
        JTextField destinationFilter = new JTextField(15);
        JButton filterButton = new JButton("Filter");

        filterPanel.add(new JLabel("Origin:"));
        filterPanel.add(originFilter);
        filterPanel.add(new JLabel("Destination:"));
        filterPanel.add(destinationFilter);
        filterPanel.add(filterButton);

        filterButton.addActionListener(e -> {
            String origin = originFilter.getText().trim();
            String destination = destinationFilter.getText().trim();
            filterRides(origin, destination);
        });

        add(filterPanel, BorderLayout.NORTH);
    }

    private void loadAvailableRides() {
        rideListModel.clear();
        List<Ride> availableRides = rideManager.getAvailableRides();
        for (Ride ride : availableRides) {
            if (ride.getAvailableSeats() > 0 && !ride.getDriverName().equals(currentUser.getFirstName())) {
                rideListModel.addElement(ride);
            }
        }
    }

    private void filterRides(String origin, String destination) {
        rideListModel.clear();
        List<Ride> availableRides = rideManager.getAvailableRides();
        for (Ride ride : availableRides) {
            if (ride.getAvailableSeats() > 0 && 
                !ride.getDriverName().equals(currentUser.getFirstName()) &&
                (origin.isEmpty() || ride.getOrigin().toLowerCase().contains(origin.toLowerCase())) &&
                (destination.isEmpty() || ride.getDestination().toLowerCase().contains(destination.toLowerCase()))) {
                rideListModel.addElement(ride);
            }
        }
    }

    private void updateRideDetails() {
        if (selectedRide == null) {
            rideDetailsArea.setText("");
            return;
        }

        StringBuilder details = new StringBuilder();
        details.append("Ride Details:\n\n");
        details.append("Driver: ").append(selectedRide.getDriverName()).append("\n");
        details.append("From: ").append(selectedRide.getOrigin()).append("\n");
        details.append("To: ").append(selectedRide.getDestination()).append("\n");
        details.append("Date: ").append(dateFormat.format(selectedRide.getDate())).append("\n");
        details.append("Available Seats: ").append(selectedRide.getAvailableSeats()).append("\n");

        rideDetailsArea.setText(details.toString());
    }

    private void bookRide() {
        if (selectedRide == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select a ride to book.",
                    "Book Ride",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (selectedRide.getAvailableSeats() <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Sorry, this ride is full.",
                    "Book Ride",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int result = JOptionPane.showConfirmDialog(this,
                "Do you want to book this ride?",
                "Confirm Booking",
                JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            selectedRide.bookSeat();
            rideManager.updateRide(selectedRide);
            JOptionPane.showMessageDialog(this,
                    "Ride booked successfully!",
                    "Book Ride",
                    JOptionPane.INFORMATION_MESSAGE);
            loadAvailableRides();
            updateRideDetails();
        }
    }

    private void cancelBooking() {
        if (selectedRide == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select a ride to cancel.",
                    "Cancel Booking",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int result = JOptionPane.showConfirmDialog(this,
                "Do you want to cancel this booking?",
                "Confirm Cancellation",
                JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            selectedRide.cancelBooking();
            rideManager.updateRide(selectedRide);
            JOptionPane.showMessageDialog(this,
                    "Booking cancelled successfully!",
                    "Cancel Booking",
                    JOptionPane.INFORMATION_MESSAGE);
            loadAvailableRides();
            updateRideDetails();
        }
    }
}
