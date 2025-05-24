import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Vector;

public class SearchRidesDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    private RideManager rideManager;
    private JTextField originField;
    private JTextField destinationField;
    private JTable ridesTable;
    private DefaultTableModel tableModel;
    private SimpleDateFormat dateFormat;
    private JPanel mainPanel;

    public SearchRidesDialog(Frame parent, RideManager rideManager) {
        super(parent, "Search Rides", true);
        this.rideManager = rideManager;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        initializeComponents();
        loadInitialRides();
    }

    private void initializeComponents() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Search Panel
        JPanel searchPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Origin field
        gbc.gridx = 0;
        gbc.gridy = 0;
        searchPanel.add(new JLabel("Origin:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        originField = new JTextField(20);
        searchPanel.add(originField, gbc);

        // Destination field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        searchPanel.add(new JLabel("Destination:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        destinationField = new JTextField(20);
        searchPanel.add(destinationField, gbc);

        // Search button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> performSearch());
        searchPanel.add(btnSearch, gbc);

        mainPanel.add(searchPanel, BorderLayout.NORTH);

        // Results table
        initializeTable();
        JScrollPane scrollPane = new JScrollPane(ridesTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> dispose());
        buttonPanel.add(btnClose);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add to dialog
        getContentPane().add(mainPanel);
        setSize(600, 400);
        setLocationRelativeTo(null);
    }

    private void initializeTable() {
        String[] columnNames = {"Driver", "From", "To", "Date", "Available Seats"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        ridesTable = new JTable(tableModel);
        ridesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void loadInitialRides() {
        updateTable(rideManager.getRides());
    }

    private void performSearch() {
        String origin = originField.getText().trim();
        String destination = destinationField.getText().trim();
        
        List<Ride> results;
        if (origin.isEmpty() && destination.isEmpty()) {
            // If no search criteria, show all rides
            results = rideManager.getRides();
        } else {
            // Otherwise, search with the provided criteria
            results = rideManager.searchRides(origin, destination);
            
            if (results.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No rides found for this route.",
                        "Search Results",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
        
        updateTable(results);
    }

    private void updateTable(List<Ride> rides) {
        tableModel.setRowCount(0);
        for (Ride ride : rides) {
            Vector<Object> row = new Vector<>();
            row.add(ride.getDriverName());
            row.add(ride.getOrigin());
            row.add(ride.getDestination());
            row.add(dateFormat.format(ride.getDate()));
            row.add(ride.getAvailableSeats());
            tableModel.addRow(row);
        }
    }
}
