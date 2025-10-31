package hostel.gui;

import hostel.data.DataManager;

import javax.swing.*;
import java.awt.*;

public class WardenLoginPanel extends JPanel {

    private DataManager dataManager;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton backButton;

    public WardenLoginPanel(DataManager dataManager, Runnable onLoginSuccess, Runnable onBack) {
        this.dataManager = dataManager;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel titleLabel = new JLabel("--- Warden Login ---");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        loginButton = new JButton("Login");
        backButton = new JButton("Back");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridy = 1;
        add(new JLabel("Enter username:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Enter password:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(passwordField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(loginButton);
        buttonPanel.add(backButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (dataManager.authenticateWarden(username, password)) {
                onLoginSuccess.run();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> onBack.run());
    }

    public void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
    }
}
