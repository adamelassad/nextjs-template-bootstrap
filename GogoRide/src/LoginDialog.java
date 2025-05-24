import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    private JTextField tfUsername;
    private JPasswordField pfPassword;
    private JLabel lbUsername;
    private JLabel lbPassword;
    private JButton btnLogin;
    private JButton btnCancel;
    private boolean succeeded;
    private UserManager userManager;
    private UserProfile currentUser;

    public LoginDialog(Frame parent, UserManager userManager) {
        super(parent, "Connexion", true);
        this.userManager = userManager;
        this.currentUser = null;

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();

        cs.fill = GridBagConstraints.HORIZONTAL;

        lbUsername = new JLabel("Username: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(lbUsername, cs);

        tfUsername = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(tfUsername, cs);

        lbPassword = new JLabel("Password: ");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        panel.add(lbPassword, cs);

        pfPassword = new JPasswordField(20);
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(pfPassword, cs);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        btnLogin = new JButton("Login");
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionLogin();
            }
        });
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        JPanel bp = new JPanel();
        bp.add(btnLogin);
        bp.add(btnCancel);

        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);

        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    private void actionLogin() {
        String username = tfUsername.getText().trim();
        String password = new String(pfPassword.getPassword());

        if (username.length() == 0) {
            JOptionPane.showMessageDialog(LoginDialog.this,
                "Veuillez entrer un nom d'utilisateur",
                "Erreur de connexion",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (password.length() == 0) {
            JOptionPane.showMessageDialog(LoginDialog.this,
                "Veuillez entrer un mot de passe",
                "Erreur de connexion",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (DatabaseManager.authenticateUser(username, password)) {
                // Get the user profile after successful authentication
                currentUser = DatabaseManager.getUserByUsername(username);
                if (currentUser != null) {
                    succeeded = true;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginDialog.this,
                        "Erreur lors de la récupération du profil utilisateur",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(LoginDialog.this,
                    "Nom d'utilisateur ou mot de passe incorrect",
                    "Erreur de connexion",
                    JOptionPane.ERROR_MESSAGE);
                pfPassword.setText("");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(LoginDialog.this,
                "Erreur de connexion à la base de données: " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return tfUsername.getText().trim();
    }

    public String getPassword() {
        return new String(pfPassword.getPassword());
    }

    public boolean isSucceeded() {
        return succeeded;
    }

    public UserProfile getCurrentUser() {
        return currentUser;
    }
}
