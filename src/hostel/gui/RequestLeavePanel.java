package hostel.gui;

import hostel.data.DataManager;
import hostel.models.LeaveRequest;
import hostel.models.Student;

import javax.swing.*;
import java.awt.*;

public class RequestLeavePanel extends JPanel {

    private DataManager dataManager;
    private Student student;
    private JTextField reasonField, dateField, hourField;

    public RequestLeavePanel(DataManager dataManager, Student student, Runnable onBack) {
        this.dataManager = dataManager;
        this.student = student;
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Request Leave", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Reason:"));
        reasonField = new JTextField();
        formPanel.add(reasonField);

        formPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        dateField = new JTextField();
        formPanel.add(dateField);

        formPanel.add(new JLabel("Hour (HH:MM):"));
        hourField = new JTextField();
        formPanel.add(hourField);

        JButton submitButton = new JButton("Submit Request");
        JButton backButton = new JButton("Back");
        formPanel.add(submitButton);
        formPanel.add(backButton);

        add(formPanel, BorderLayout.CENTER);

        // Action Listeners
        submitButton.addActionListener(e -> submitRequest());
        backButton.addActionListener(e -> onBack.run());
    }

    private void submitRequest() {
        String reason = reasonField.getText();
        String date = dateField.getText();
        String hour = hourField.getText();

        if (reason.isEmpty() || date.isEmpty() || hour.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled out.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LeaveRequest request = new LeaveRequest(student.getRollNumber(), reason, date, hour);
        dataManager.addLeaveRequest(request);

        JOptionPane.showMessageDialog(this, "Leave request submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        // Clear fields
        reasonField.setText("");
        dateField.setText("");
        hourField.setText("");
    }
}
