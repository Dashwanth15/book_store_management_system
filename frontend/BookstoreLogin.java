package frontend;

import backend.BookInventory;
import java.awt.*;
import javax.swing.*;

public class BookstoreLogin extends JFrame {
    private JRadioButton userRadio, adminRadio;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JPanel userPanel;
    private BookInventory inventory;

    public BookstoreLogin(BookInventory inventory) {
        this.inventory = inventory;

        setTitle("Online Bookstore Management System");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Online Bookstore Management System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        JPanel radioPanel = new JPanel();
        userRadio = new JRadioButton("User");
        adminRadio = new JRadioButton("Admin");
        ButtonGroup group = new ButtonGroup();
        group.add(userRadio);
        group.add(adminRadio);
        userRadio.setSelected(true);
        radioPanel.add(userRadio);
        radioPanel.add(adminRadio);
        add(radioPanel, BorderLayout.CENTER);

        userPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        userPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
        userPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        userPanel.add(usernameField);

        userPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        userPanel.add(passwordField);

        loginButton = new JButton("Login");
        userPanel.add(new JLabel());
        userPanel.add(loginButton);
        add(userPanel, BorderLayout.SOUTH);

        userRadio.addActionListener(e -> toggleUserAdmin(true));
        adminRadio.addActionListener(e -> toggleUserAdmin(false));
        loginButton.addActionListener(e -> loginAction());

        setVisible(true);
    }

    private void toggleUserAdmin(boolean isUser) {
        usernameField.setEnabled(isUser);
        if (!isUser) {
            usernameField.setText("");
        }
    }

    private void loginAction() {
        try {
            if (userRadio.isSelected()) {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());
                validateUsername(username);
                validatePassword(password);
                JOptionPane.showMessageDialog(this, "User login successful!");
                new UserInterface(inventory);
                this.dispose();
            } else {
                String password = new String(passwordField.getPassword());
                validatePassword(password);
                JOptionPane.showMessageDialog(this, "Admin login successful!");
                new AdminInterface(inventory);
                this.dispose();
            }
        } catch (InvalidUsernameException | InvalidPasswordException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void validateUsername(String username) throws InvalidUsernameException {
        if (username.isEmpty())
            throw new InvalidUsernameException("Username cannot be empty.");
        if (!username.matches("[a-zA-Z]+"))
            throw new InvalidUsernameException("Username must contain only letters.");
    }

    private void validatePassword(String password) throws InvalidPasswordException {
        if (password.length() < 5)
            throw new InvalidPasswordException("Password must be at least 5 characters.");
        if (!password.matches(".*\\d.*"))
            throw new InvalidPasswordException("Password must contain at least one number.");
        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*"))
            throw new InvalidPasswordException("Password must contain one special character.");
    }

    // Custom Exceptions as inner classes
    static class InvalidUsernameException extends Exception {
        public InvalidUsernameException(String message) {
            super(message);
        }
    }

    static class InvalidPasswordException extends Exception {
        public InvalidPasswordException(String message) {
            super(message);
        }
    }
}

