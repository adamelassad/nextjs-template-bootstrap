package covoitu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class RegisterDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    private JTextField usernameField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private boolean succeeded;
    private UserManager userManager;

    public RegisterDialog(Frame parent, UserManager userManager) {
        super(parent, "Inscription", true);
        this.userManager = userManager;

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();

        cs.fill = GridBagConstraints.HORIZONTAL;

        // Username field
        JLabel lbUsername = new JLabel("Nom d'utilisateur: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(lbUsername, cs);

        usernameField = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(usernameField, cs);

        // First Name field
        JLabel lbFirstName = new JLabel("Prénom: ");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        panel.add(lbFirstName, cs);

        firstNameField = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(firstNameField, cs);

        // Last Name field
        JLabel lbLastName = new JLabel("Nom: ");
        cs.gridx = 0;
        cs.gridy = 2;
        cs.gridwidth = 1;
        panel.add(lbLastName, cs);

        lastNameField = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 2;
        cs.gridwidth = 2;
        panel.add(lastNameField, cs);

        // Password field
        JLabel lbPassword = new JLabel("Mot de passe: ");
        cs.gridx = 0;
        cs.gridy = 3;
        cs.gridwidth = 1;
        panel.add(lbPassword, cs);

        passwordField = new JPasswordField(20);
        cs.gridx = 1;
        cs.gridy = 3;
        cs.gridwidth = 2;
        panel.add(passwordField, cs);

        // Confirm Password field
        JLabel lbConfirmPassword = new JLabel("Confirmer le mot de passe: ");
        cs.gridx = 0;
        cs.gridy = 4;
        cs.gridwidth = 1;
        panel.add(lbConfirmPassword, cs);

        confirmPasswordField = new JPasswordField(20);
        cs.gridx = 1;
        cs.gridy = 4;
        cs.gridwidth = 2;
        panel.add(confirmPasswordField, cs);

        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JButton btnRegister = new JButton("S'inscrire");

        btnRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (validateFields()) {
                    try {
                        // Check if username already exists
                        if (DatabaseManager.isUsernameExists(getUsername())) {
                            JOptionPane.showMessageDialog(RegisterDialog.this,
                                "Ce nom d'utilisateur existe déjà. Veuillez en choisir un autre.",
                                "Erreur d'inscription",
                                JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        // Create new user profile
                        String userId = java.util.UUID.randomUUID().toString();
                        UserProfile newUser = new UserProfile(
                            userId,
                            getUsername(),
                            getFirstName(),
                            getLastName()
                        );

                        // Save user to database
                        DatabaseManager.saveUser(newUser, getPassword());
                        succeeded = true;
                        JOptionPane.showMessageDialog(RegisterDialog.this,
                            "Inscription réussie! Vous pouvez maintenant vous connecter.",
                            "Inscription",
                            JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(RegisterDialog.this,
                            "Erreur lors de l'inscription: " + ex.getMessage(),
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(RegisterDialog.this,
                        "Veuillez remplir tous les champs correctement et vérifier que les mots de passe correspondent.",
                        "Erreur d'inscription",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton btnCancel = new JButton("Annuler");
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                succeeded = false;
                dispose();
            }
        });

        JPanel bp = new JPanel();
        bp.add(btnRegister);
        bp.add(btnCancel);

        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);

        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    private boolean validateFields() {
        if (usernameField.getText().trim().isEmpty()) return false;
        if (firstNameField.getText().trim().isEmpty()) return false;
        if (lastNameField.getText().trim().isEmpty()) return false;
        
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        if (password.isEmpty() || !password.equals(confirmPassword)) return false;
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this,
                "Le mot de passe doit contenir au moins 6 caractères.",
                "Validation",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }

    public String getUsername() {
        return usernameField.getText().trim();
    }

    public String getFirstName() {
        return firstNameField.getText().trim();
    }

    public String getLastName() {
        return lastNameField.getText().trim();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public boolean isSucceeded() {
        return succeeded;
    }
}
