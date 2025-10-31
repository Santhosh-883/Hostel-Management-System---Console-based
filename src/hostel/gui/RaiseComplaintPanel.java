package hostel.gui;

import hostel.data.DataManager;
import hostel.models.Complaint;
import hostel.models.Student;

import javax.swing.*;
import java.awt.*;

public class RaiseComplaintPanel extends JPanel {

    private DataManager dataManager;
    private Student student;
    private JTextArea descriptionArea;

    public RaiseComplaintPanel(DataManager dataManager, Student student, Runnable onBack) {
        this.dataManager = dataManager;
        this.student = student;
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Raise a Complaint", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Description:"), BorderLayout.NORTH);
        descriptionArea = new JTextArea(5, 20);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        formPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton submitButton = new JButton("Submit Complaint");
        JButton backButton = new JButton("Back");
        buttonPanel.add(submitButton);
        buttonPanel.add(backButton);
        formPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(formPanel, BorderLayout.CENTER);

        // Action Listeners
        submitButton.addActionListener(e -> submitComplaint());
        backButton.addActionListener(e -> onBack.run());
    }

    private void submitComplaint() {
        String description = descriptionArea.getText();

        if (description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a description for your complaint.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Complaint complaint = new Complaint(student.getRollNumber(), description);
        dataManager.addComplaint(complaint);

        JOptionPane.showMessageDialog(this, "Complaint submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        // Clear field
        descriptionArea.setText("");
    }
}
