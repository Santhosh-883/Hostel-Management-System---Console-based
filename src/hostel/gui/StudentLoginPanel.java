package hostel.gui;

import hostel.data.DataManager;
import hostel.models.Student;

import javax.swing.*;
import java.awt.*;

public class StudentLoginPanel extends JPanel {

    private DataManager dataManager;
    private JTextField rollNumberField;
    private JButton loginButton;
    private JButton backButton;

    public StudentLoginPanel(DataManager dataManager, java.util.function.Consumer<Student> onLoginSuccess, Runnable onBack) {
        this.dataManager = dataManager;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel titleLabel = new JLabel("--- Student Login ---");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        rollNumberField = new JTextField(15);
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
        add(new JLabel("Enter your roll number to login:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(rollNumberField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(loginButton);
        buttonPanel.add(backButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);

        loginButton.addActionListener(e -> {
            String rollNumber = rollNumberField.getText();
            Student student = dataManager.getStudent(rollNumber);
            if (student != null) {
                onLoginSuccess.accept(student);
            } else {
                JOptionPane.showMessageDialog(this, "Student with that roll number not found.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> onBack.run());
    }

    public void clearFields() {
        rollNumberField.setText("");
    }
}
