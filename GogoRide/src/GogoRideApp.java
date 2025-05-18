import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GogoRideApp extends JFrame {
    private RideManager rideManager;

    public GogoRideApp() {
        rideManager = new RideManager();

        setTitle("GogoRide - Carpooling Application");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {
        // Main panel
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Header label
        JLabel headerLabel = new JLabel("Welcome to GogoRide", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(headerLabel, BorderLayout.NORTH);

        // Placeholder center content
        JLabel contentLabel = new JLabel("Carpooling app main content will be here.", SwingConstants.CENTER);
        contentLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(contentLabel, BorderLayout.CENTER);

        // Footer with buttons
        JPanel footerPanel = new JPanel();
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        JButton createRideButton = new JButton("Create Ride");
        JButton searchRideButton = new JButton("Search Rides");
        footerPanel.add(loginButton);
        footerPanel.add(registerButton);
        footerPanel.add(createRideButton);
        footerPanel.add(searchRideButton);

        panel.add(footerPanel, BorderLayout.SOUTH);

        add(panel);

        // Action listeners for buttons (placeholders)
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LoginDialog loginDlg = new LoginDialog(GogoRideApp.this);
                loginDlg.setVisible(true);
                if (loginDlg.isSucceeded()) {
                    JOptionPane.showMessageDialog(GogoRideApp.this,
                            "Hi " + loginDlg.getUsername() + "! You have successfully logged in.",
                            "Login",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(GogoRideApp.this, "Register functionality to be implemented.");
            }
        });

        createRideButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CreateRideDialog createRideDlg = new CreateRideDialog(GogoRideApp.this);
                createRideDlg.setVisible(true);
                if (createRideDlg.isSucceeded()) {
                    Ride newRide = new Ride(
                            createRideDlg.getDriverName(),
                            createRideDlg.getOrigin(),
                            createRideDlg.getDestination(),
                            createRideDlg.getDate(),
                            createRideDlg.getAvailableSeats()
                    );
                    rideManager.addRide(newRide);
                    JOptionPane.showMessageDialog(GogoRideApp.this,
                            "Ride created successfully!",
                            "Create Ride",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        searchRideButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SearchRidesDialog searchRidesDlg = new SearchRidesDialog(GogoRideApp.this, rideManager);
                searchRidesDlg.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GogoRideApp app = new GogoRideApp();
            app.setVisible(true);
        });
    }
}
