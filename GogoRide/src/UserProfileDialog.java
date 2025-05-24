import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserProfileDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    private UserProfile userProfile;
    private boolean succeeded;

    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField addressField;
    private JComboBox<String> userTypeCombo;
    private JLabel ratingLabel;
    private JLabel totalRidesLabel;

    public UserProfileDialog(Frame parent, UserProfile profile) {
        super(parent, "User Profile", true);
        this.userProfile = profile;

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();
        cs.fill = GridBagConstraints.HORIZONTAL;
        cs.insets = new Insets(5, 5, 5, 5);

        // Personal Information Section
        JLabel sectionLabel = new JLabel("Personal Information");
        sectionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(sectionLabel, cs);

        // First Name
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        panel.add(new JLabel("First Name:"), cs);

        firstNameField = new JTextField(20);
        firstNameField.setText(profile.getFirstName());
        cs.gridx = 1;
        panel.add(firstNameField, cs);

        // Last Name
        cs.gridx = 0;
        cs.gridy = 2;
        panel.add(new JLabel("Last Name:"), cs);

        lastNameField = new JTextField(20);
        lastNameField.setText(profile.getLastName());
        cs.gridx = 1;
        panel.add(lastNameField, cs);

        // Email
        cs.gridx = 0;
        cs.gridy = 3;
        panel.add(new JLabel("Email:"), cs);

        emailField = new JTextField(20);
        emailField.setText(profile.getEmail());
        cs.gridx = 1;
        panel.add(emailField, cs);

        // Phone
        cs.gridx = 0;
        cs.gridy = 4;
        panel.add(new JLabel("Phone:"), cs);

        phoneField = new JTextField(20);
        phoneField.setText(profile.getPhoneNumber());
        cs.gridx = 1;
        panel.add(phoneField, cs);

        // Address
        cs.gridx = 0;
        cs.gridy = 5;
        panel.add(new JLabel("Address:"), cs);

        addressField = new JTextField(20);
        addressField.setText(profile.getAddress());
        cs.gridx = 1;
        panel.add(addressField, cs);

        // User Type
        cs.gridx = 0;
        cs.gridy = 6;
        panel.add(new JLabel("User Type:"), cs);

        userTypeCombo = new JComboBox<>(new String[]{"PASSENGER", "DRIVER"});
        userTypeCombo.setSelectedItem(profile.getUserType());
        cs.gridx = 1;
        panel.add(userTypeCombo, cs);

        // Statistics Section
        JLabel statsLabel = new JLabel("Statistics");
        statsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        cs.gridx = 0;
        cs.gridy = 7;
        cs.gridwidth = 2;
        panel.add(statsLabel, cs);

        // Rating
        cs.gridx = 0;
        cs.gridy = 8;
        cs.gridwidth = 1;
        panel.add(new JLabel("Rating:"), cs);

        ratingLabel = new JLabel(String.format("%.1f/5.0", profile.getRating()));
        cs.gridx = 1;
        panel.add(ratingLabel, cs);

        // Total Rides
        cs.gridx = 0;
        cs.gridy = 9;
        panel.add(new JLabel("Total Rides:"), cs);

        totalRidesLabel = new JLabel(String.valueOf(profile.getTotalRides()));
        cs.gridx = 1;
        panel.add(totalRidesLabel, cs);

        // Registration Date
        cs.gridx = 0;
        cs.gridy = 10;
        panel.add(new JLabel("Member Since:"), cs);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        JLabel regDateLabel = new JLabel(sdf.format(profile.getRegistrationDate()));
        cs.gridx = 1;
        panel.add(regDateLabel, cs);

        // Buttons
        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (validateFields()) {
                    updateProfile();
                    succeeded = true;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(UserProfileDialog.this,
                            "Please fill all fields correctly",
                            "Profile Update",
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
        bp.add(btnSave);
        bp.add(btnCancel);

        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);

        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    private boolean validateFields() {
        return !firstNameField.getText().trim().isEmpty() &&
               !lastNameField.getText().trim().isEmpty() &&
               !emailField.getText().trim().isEmpty() &&
               !phoneField.getText().trim().isEmpty() &&
               !addressField.getText().trim().isEmpty();
    }

    private void updateProfile() {
        userProfile.setFirstName(firstNameField.getText().trim());
        userProfile.setLastName(lastNameField.getText().trim());
        userProfile.setEmail(emailField.getText().trim());
        userProfile.setPhoneNumber(phoneField.getText().trim());
        userProfile.setAddress(addressField.getText().trim());
        userProfile.setUserType((String) userTypeCombo.getSelectedItem());
    }

    public boolean isSucceeded() {
        return succeeded;
    }

    public UserProfile getProfile() {
        return userProfile;
    }
}
