package hostel.gui;

import hostel.data.DataManager;
import hostel.models.Poll;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.UUID;

public class CreatePollPanel extends JPanel {

    private DataManager dataManager;
    private JTextField titleField, questionField, optionsField;

    public CreatePollPanel(DataManager dataManager, Runnable onBack) {
        this.dataManager = dataManager;
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Create a New Poll", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Poll Title:"));
        titleField = new JTextField();
        formPanel.add(titleField);

        formPanel.add(new JLabel("Question:"));
        questionField = new JTextField();
        formPanel.add(questionField);

        formPanel.add(new JLabel("Options (comma-separated):"));
        optionsField = new JTextField();
        formPanel.add(optionsField);

        JButton createButton = new JButton("Create Poll");
        JButton backButton = new JButton("Back");
        formPanel.add(createButton);
        formPanel.add(backButton);

        add(formPanel, BorderLayout.CENTER);

        // Action Listeners
        createButton.addActionListener(e -> createPoll());
        backButton.addActionListener(e -> onBack.run());
    }

    private void createPoll() {
        String title = titleField.getText();
        String question = questionField.getText();
        String optionsStr = optionsField.getText();

        if (title.isEmpty() || question.isEmpty() || optionsStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled out.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        java.util.List<String> options = Arrays.asList(optionsStr.split(","));
        String pollId = UUID.randomUUID().toString();
        // Assuming the creator warden is 'admin' for simplicity
        Poll poll = new Poll(pollId, title, question, options, "admin");
        dataManager.addPoll(poll);

        JOptionPane.showMessageDialog(this, "Poll created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        // Clear fields
        titleField.setText("");
        questionField.setText("");
        optionsField.setText("");
    }
}
