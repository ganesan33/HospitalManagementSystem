import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginPage() {
        setTitle("Hospital Management System - Login");
        setSize(400, 250); // Set the frame to a fixed size initially
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen at start
        setLayout(new GridBagLayout()); // Center the content

        // Create a fixed-size inner panel to hold the login components
        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new GridLayout(3, 2, 10, 10));
        innerPanel.setPreferredSize(new Dimension(300, 150)); // Fixed size for inner panel

        // Set custom font
        Font labelFont = new Font("Arial", Font.BOLD, 16);
        Font inputFont = new Font("Arial", Font.PLAIN, 14);
        Font buttonFont = new Font("Arial", Font.BOLD, 16);

        // Username label and field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(labelFont);
        usernameField = new JTextField();
        usernameField.setFont(inputFont);

        // Password label and field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(labelFont);
        passwordField = new JPasswordField();
        passwordField.setFont(inputFont);

        // Login button
        loginButton = new JButton("Login");
        loginButton.setFont(buttonFont);
        loginButton.addActionListener(new LoginActionListener());

        // Add components to inner panel
        innerPanel.add(usernameLabel);
        innerPanel.add(usernameField);
        innerPanel.add(passwordLabel);
        innerPanel.add(passwordField);
        innerPanel.add(new JLabel()); // Placeholder to align the button to the right
        innerPanel.add(loginButton);

        // Add the inner panel to the frame and center it
        add(innerPanel, new GridBagConstraints());
    }

    // Action listener for login button
    private class LoginActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (validateLogin(username, password)) {
                JOptionPane.showMessageDialog(LoginPage.this, "Login successful!");
                new HospitalManagementSystemGUI().setVisible(true);
                dispose(); // Close login page
            } else {
                JOptionPane.showMessageDialog(LoginPage.this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private boolean validateLogin(String username, String password) {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                try (ResultSet rs = stmt.executeQuery()) {
                    return rs.next();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(LoginPage.this, "Database error: " + ex.getMessage());
                return false;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginPage().setVisible(true);
        });
    }
}