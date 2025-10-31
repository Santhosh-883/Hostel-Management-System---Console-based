package hostel.gui;

import hostel.data.DataManager;
import hostel.models.Student;

import javax.swing.*;
import java.awt.*;

public class AddStudentPanel extends JPanel {

    private DataManager dataManager;
    private JTextField rollNumberField, nameField, roomNumberField, phoneNumberField;

    public AddStudentPanel(DataManager dataManager, Runnable onBack) {
        this.dataManager = dataManager;
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Add Student", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Roll Number:"));
        rollNumberField = new JTextField();
        formPanel.add(rollNumberField);

        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Room Number:"));
        roomNumberField = new JTextField();
        formPanel.add(roomNumberField);

        formPanel.add(new JLabel("Phone Number:"));
        phoneNumberField = new JTextField();
        formPanel.add(phoneNumberField);

        JButton addButton = new JButton("Add Student");
        JButton backButton = new JButton("Back");
        formPanel.add(addButton);
        formPanel.add(backButton);

        add(formPanel, BorderLayout.CENTER);

        // Action Listeners
        addButton.addActionListener(e -> addStudent());
        backButton.addActionListener(e -> onBack.run());
    }

    private void addStudent() {
        String rollNumber = rollNumberField.getText();
        String name = nameField.getText();
        String roomNumber = roomNumberField.getText();
        String phoneNumber = phoneNumberField.getText();

        if (rollNumber.isEmpty() || name.isEmpty() || roomNumber.isEmpty() || phoneNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled out.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Student student = new Student(name, rollNumber, roomNumber, phoneNumber);
        dataManager.addStudent(student);

        JOptionPane.showMessageDialog(this, "Student added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        // Clear fields
        rollNumberField.setText("");
        nameField.setText("");
        roomNumberField.setText("");
        phoneNumberField.setText("");
    }
}
