import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.UUID;

public class GogoRideApp extends JFrame {
    private static final long serialVersionUID = 1L;
    private RideManager rideManager;
    private UserManager userManager;
    private MessageManager messageManager;
    private UserProfile currentUser;

    public GogoRideApp() {
        rideManager = new RideManager();
        userManager = new UserManager();
        messageManager = new MessageManager();
        currentUser = null;

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
        // Add logo image and title in header panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ImageIcon logoIcon = new ImageIcon("logo.jpg");
        JLabel logoLabel = new JLabel(logoIcon);
        headerPanel.add(logoLabel);

        JLabel headerLabel = new JLabel("GogoRide - Plateforme de covoiturage écologique");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(headerLabel);

        panel.add(headerPanel, BorderLayout.NORTH);

        // Updated center content inspired by the provided image
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(245, 248, 250));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Voyagez écologique avec GogoRide");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("La plateforme de covoiturage qui met l'écologie au centre de vos déplacements.");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        centerPanel.add(subtitleLabel);

        // Removed the sign-up and login buttons as per user request
        // JPanel buttonPanel = new JPanel();
        // buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        // buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // JButton signUpButton = new JButton("S'inscrire");
        // JButton loginButton = new JButton("Se connecter");
        // buttonPanel.add(signUpButton);
        // buttonPanel.add(loginButton);
        // centerPanel.add(buttonPanel);

        panel.add(centerPanel, BorderLayout.CENTER);

        // Footer with buttons
        JPanel footerPanel = new JPanel();
        JButton loginButtonFooter = new JButton("Login");
        JButton registerButton = new JButton("Register");
        JButton createRideButton = new JButton("Create Ride");
        JButton searchRideButton = new JButton("Search Rides");
        JButton profileButton = new JButton("Profile");
        JButton logoutButton = new JButton("Logout");

        footerPanel.add(loginButtonFooter);
        footerPanel.add(registerButton);
        footerPanel.add(createRideButton);
        footerPanel.add(searchRideButton);
        footerPanel.add(profileButton);
        footerPanel.add(logoutButton);

        panel.add(footerPanel, BorderLayout.SOUTH);

        add(panel);

        // Action listeners for buttons
        loginButtonFooter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LoginDialog loginDlg = new LoginDialog(GogoRideApp.this, userManager);
                loginDlg.setVisible(true);
                if (loginDlg.isSucceeded()) {
                    // Simulate authentication and set current user
                    String userId = UUID.randomUUID().toString();
                    currentUser = new UserProfile(userId, loginDlg.getUsername(), "", "");
                    userManager.addUser(currentUser);
                    JOptionPane.showMessageDialog(GogoRideApp.this,
                            "Hi " + loginDlg.getUsername() + "! You have successfully logged in.",
                            "Login",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RegisterDialog registerDlg = new RegisterDialog(GogoRideApp.this);
                registerDlg.setVisible(true);
                if (registerDlg.isSucceeded()) {
                    String userId = UUID.randomUUID().toString();
                    currentUser = new UserProfile(userId, registerDlg.getUsername(), "", "");
                    userManager.addUser(currentUser);
                    JOptionPane.showMessageDialog(GogoRideApp.this,
                            "User " + registerDlg.getUsername() + " registered successfully!",
                            "Register",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        createRideButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentUser == null) {
                    JOptionPane.showMessageDialog(GogoRideApp.this,
                            "Please login to create a ride.",
                            "Create Ride",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
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
                if (currentUser == null) {
                    JOptionPane.showMessageDialog(GogoRideApp.this,
                            "Please login to search rides.",
                            "Search Rides",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                SearchRidesDialog searchRidesDlg = new SearchRidesDialog(GogoRideApp.this, rideManager);
                searchRidesDlg.setVisible(true);
            }
        });

        // Add Book Ride button
        JButton bookRideButton = new JButton("Book Ride");
        footerPanel.add(bookRideButton);

        bookRideButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentUser == null) {
                    JOptionPane.showMessageDialog(GogoRideApp.this,
                            "Please login to book a ride.",
                            "Book Ride",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                RideBookingDialog bookingDlg = new RideBookingDialog(GogoRideApp.this, currentUser, rideManager);
                bookingDlg.setVisible(true);
            }
        });

        // Add messaging button and functionality
        JButton messageButton = new JButton("Messages");
        footerPanel.add(messageButton);

        messageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentUser == null) {
                    JOptionPane.showMessageDialog(GogoRideApp.this,
                            "Please login to access messages.",
                            "Messages",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                MessageDialog messageDlg = new MessageDialog(GogoRideApp.this, currentUser, messageManager, userManager);
                messageDlg.setVisible(true);
            }
        });

        profileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentUser == null) {
                    JOptionPane.showMessageDialog(GogoRideApp.this,
                            "Please login to view your profile.",
                            "Profile",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                UserProfileDialog profileDlg = new UserProfileDialog(GogoRideApp.this, currentUser);
                profileDlg.setVisible(true);
                if (profileDlg.isSucceeded()) {
                    currentUser = profileDlg.getProfile();
                    userManager.updateUser(currentUser);
                    JOptionPane.showMessageDialog(GogoRideApp.this,
                            "Profile updated successfully!",
                            "Profile",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentUser == null) {
                    JOptionPane.showMessageDialog(GogoRideApp.this,
                            "No user is currently logged in.",
                            "Logout",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                currentUser = null;
                JOptionPane.showMessageDialog(GogoRideApp.this,
                        "You have been logged out.",
                        "Logout",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    public static void main(String[] args) {
        // Set up database connection before starting the application
        try {
            DatabaseManager.connect();
            JOptionPane.showMessageDialog(null,
                "Connexion à la base de données réussie !",
                "Statut de connexion",
                JOptionPane.INFORMATION_MESSAGE);

            SwingUtilities.invokeLater(() -> {
                GogoRideApp app = new GogoRideApp();
                app.setVisible(true);
            });

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Erreur de connexion à la base de données : " + e.getMessage() + 
                "\nVérifiez que:\n" +
                "1. XAMPP est démarré (MySQL et Apache)\n" +
                "2. La base de données 'gogoride' existe\n" +
                "3. Les identifiants de connexion sont corrects",
                "Erreur de connexion",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1); // Exit if database connection fails
        }

        // Add shutdown hook to close database connection
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                DatabaseManager.close();
                System.out.println("Database connection closed.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }
}
