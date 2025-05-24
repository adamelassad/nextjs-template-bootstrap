import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateRideDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    private JTextField driverNameField;
    private JTextField originField;
    private JTextField destinationField;
    private JTextField dateField;
    private JTextField seatsField;
    private boolean succeeded;

    public CreateRideDialog(Frame parent) {
        super(parent, "Create Ride", true);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();

        cs.fill = GridBagConstraints.HORIZONTAL;

        JLabel lbDriverName = new JLabel("Driver Name: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(lbDriverName, cs);

        driverNameField = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(driverNameField, cs);

        JLabel lbOrigin = new JLabel("Origin: ");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        panel.add(lbOrigin, cs);

        originField = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(originField, cs);

        JLabel lbDestination = new JLabel("Destination: ");
        cs.gridx = 0;
        cs.gridy = 2;
        cs.gridwidth = 1;
        panel.add(lbDestination, cs);

        destinationField = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 2;
        cs.gridwidth = 2;
        panel.add(destinationField, cs);

        JLabel lbDate = new JLabel("Date (YYYY-MM-DD): ");
        cs.gridx = 0;
        cs.gridy = 3;
        cs.gridwidth = 1;
        panel.add(lbDate, cs);

        dateField = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 3;
        cs.gridwidth = 2;
        panel.add(dateField, cs);

        JLabel lbSeats = new JLabel("Available Seats: ");
        cs.gridx = 0;
        cs.gridy = 4;
        cs.gridwidth = 1;
        panel.add(lbSeats, cs);

        seatsField = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 4;
        cs.gridwidth = 2;
        panel.add(seatsField, cs);

        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JButton btnCreate = new JButton("Create");

        btnCreate.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (validateFields()) {
                    succeeded = true;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(CreateRideDialog.this,
                            "Please fill all fields correctly.",
                            "Create Ride",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                succeeded = false;
                dispose();
            }
        });
        JPanel bp = new JPanel();
        bp.add(btnCreate);
        bp.add(btnCancel);

        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);

        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    private boolean validateFields() {
        if (driverNameField.getText().trim().isEmpty()) return false;
        if (originField.getText().trim().isEmpty()) return false;
        if (destinationField.getText().trim().isEmpty()) return false;
        if (dateField.getText().trim().isEmpty()) return false;
        try {
            int seats = Integer.parseInt(seatsField.getText().trim());
            if (seats < 1) return false;
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public String getDriverName() {
        return driverNameField.getText().trim();
    }

    public String getOrigin() {
        return originField.getText().trim();
    }

    public String getDestination() {
        return destinationField.getText().trim();
    }

    public String getDate() {
        return dateField.getText().trim();
    }

    public int getAvailableSeats() {
        return Integer.parseInt(seatsField.getText().trim());
    }

    public boolean isSucceeded() {
        return succeeded;
    }
}
