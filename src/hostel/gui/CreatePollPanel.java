package hostel.gui;

import hostel.data.DataManager;
import hostel.models.Poll;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreatePollPanel extends JPanel {

    private final DataManager dataManager;
    private JTextField titleField, questionField;
    private final JPanel optionsPanel;
    private final List<JTextField> optionFields = new ArrayList<>();

    public CreatePollPanel(DataManager dataManager, Runnable onBack) {
        this.dataManager = dataManager;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Create a New Poll", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Poll Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(new JLabel("Poll Title:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        titleField = new JTextField();
        formPanel.add(titleField, gbc);

        // Question
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Question:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        questionField = new JTextField();
        formPanel.add(questionField, gbc);

        // Options
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Options:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weighty = 1.0; // Allow vertical expansion
        gbc.fill = GridBagConstraints.BOTH;

        optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(optionsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(300, 100)); // Set a reasonable preferred size

        formPanel.add(scrollPane, gbc);

        gbc.weighty = 0; // Reset weighty for subsequent components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add initial options
        addOptionField();
        addOptionField();

        // Add Option Button
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        JButton addOptionButton = new JButton("Add Option");
        formPanel.add(addOptionButton, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton createButton = new JButton("Create Poll");
        JButton backButton = new JButton("Back");
        buttonPanel.add(createButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        addOptionButton.addActionListener(e -> addOptionField());
        createButton.addActionListener(e -> createPoll());
        backButton.addActionListener(e -> onBack.run());
    }

    private void addOptionField() {
        JPanel optionEntryPanel = new JPanel(new BorderLayout(5, 0));
        JTextField optionField = new JTextField(20);
        optionFields.add(optionField);
        optionEntryPanel.add(optionField, BorderLayout.CENTER);

        JButton removeButton = new JButton("-");
        removeButton.addActionListener(e -> {
            if (optionFields.size() > 2) {
                optionsPanel.remove(optionEntryPanel);
                optionFields.remove(optionField);
                optionsPanel.revalidate();
                optionsPanel.repaint();
            }
        });
        optionEntryPanel.add(removeButton, BorderLayout.EAST);

        optionsPanel.add(optionEntryPanel);
        optionsPanel.revalidate();
        optionsPanel.repaint();
    }

    private void createPoll() {
        String title = titleField.getText().trim();
        String question = questionField.getText().trim();

        if (title.isEmpty() || question.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Poll title and question cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<String> options = new ArrayList<>();
        for (JTextField optionField : optionFields) {
            String optionText = optionField.getText().trim();
            if (!optionText.isEmpty()) {
                options.add(optionText);
            }
        }

        if (options.size() < 2) {
            JOptionPane.showMessageDialog(this, "A poll must have at least two options.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String pollId = UUID.randomUUID().toString();
        Poll poll = new Poll(pollId, title, question, options, "admin"); // Assuming creator is 'admin'
        dataManager.addPoll(poll);
        dataManager.addNotificationForAllStudents("New poll available: " + title);

        JOptionPane.showMessageDialog(this, "Poll created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        // Clear fields
        clearForm();
    }

    private void clearForm() {
        titleField.setText("");
        questionField.setText("");
        optionsPanel.removeAll();
        optionFields.clear();
        addOptionField();
        addOptionField();
        optionsPanel.revalidate();
        optionsPanel.repaint();
    }
}
