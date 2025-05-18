import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class SearchRidesDialog extends JDialog {
    private JTextField originField;
    private JTextField destinationField;
    private JTable resultsTable;
    private DefaultTableModel tableModel;

    public SearchRidesDialog(Frame parent, RideManager rideManager) {
        super(parent, "Search Rides", true);

        JPanel panel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();
        cs.fill = GridBagConstraints.HORIZONTAL;

        JLabel lbOrigin = new JLabel("Origin: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        inputPanel.add(lbOrigin, cs);

        originField = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        inputPanel.add(originField, cs);

        JLabel lbDestination = new JLabel("Destination: ");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        inputPanel.add(lbDestination, cs);

        destinationField = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        inputPanel.add(destinationField, cs);

        JButton btnSearch = new JButton("Search");
        cs.gridx = 1;
        cs.gridy = 2;
        cs.gridwidth = 1;
        inputPanel.add(btnSearch, cs);

        panel.add(inputPanel, BorderLayout.NORTH);

        // Table for results
        String[] columnNames = {"Driver", "Origin", "Destination", "Date", "Available Seats"};
        tableModel = new DefaultTableModel(columnNames, 0);
        resultsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String origin = originField.getText().trim();
                String destination = destinationField.getText().trim();
                if (origin.isEmpty() || destination.isEmpty()) {
                    JOptionPane.showMessageDialog(SearchRidesDialog.this,
                            "Please enter both origin and destination.",
                            "Search Rides",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                List<Ride> results = rideManager.searchRides(origin, destination);
                updateTable(results);
            }
        });

        getContentPane().add(panel);
        setSize(600, 400);
        setLocationRelativeTo(parent);
    }

    private void updateTable(List<Ride> rides) {
        tableModel.setRowCount(0);
        for (Ride ride : rides) {
            Object[] row = {
                    ride.getDriverName(),
                    ride.getOrigin(),
                    ride.getDestination(),
                    ride.getDate(),
                    ride.getAvailableSeats()
            };
            tableModel.addRow(row);
        }
    }
}
