package hostel.gui;

import hostel.models.Student;

import javax.swing.*;
import java.awt.*;

public class ViewPersonalDetailsPanel extends JPanel {

    public ViewPersonalDetailsPanel(Student student, Runnable onBack) {
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Personal Details", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // Details Panel
        JPanel detailsPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        detailsPanel.add(new JLabel("Name:"));
        detailsPanel.add(new JLabel(student.getName()));

        detailsPanel.add(new JLabel("Roll Number:"));
        detailsPanel.add(new JLabel(student.getRollNumber()));

        detailsPanel.add(new JLabel("Room Number:"));
        detailsPanel.add(new JLabel(student.getRoomNumber()));

        detailsPanel.add(new JLabel("Phone Number:"));
        detailsPanel.add(new JLabel(student.getPhoneNumber()));

        add(detailsPanel, BorderLayout.CENTER);

        // Back Button
        JButton backButton = new JButton("Back");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listener
        backButton.addActionListener(e -> onBack.run());
    }
}
