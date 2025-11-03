package hostel.gui;

import hostel.data.DataManager;
import hostel.models.Poll;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

public class ViewPollResultsPanel extends JPanel {

    private DataManager dataManager;
    private JComboBox<String> pollComboBox;
    private JTable resultsTable;
    private DefaultTableModel tableModel;

    public ViewPollResultsPanel(DataManager dataManager, Runnable onBack) {
        this.dataManager = dataManager;
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("View Poll Results", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // Poll Selection
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pollComboBox = new JComboBox<>();
        loadPollsIntoComboBox();
        selectionPanel.add(new JLabel("Select a Poll:"));
        selectionPanel.add(pollComboBox);
        add(selectionPanel, BorderLayout.NORTH);

        // Results Table
        String[] columnNames = {"Option", "Votes", "Percentage"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        resultsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        add(scrollPane, BorderLayout.CENTER);

        // Back Button
        JButton backButton = new JButton("Back");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        pollComboBox.addActionListener(e -> displayPollResults());
        backButton.addActionListener(e -> onBack.run());

        // Reload data when the panel is shown
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                loadPollsIntoComboBox();
                displayPollResults();
            }
        });
    }

    private void loadPollsIntoComboBox() {
        pollComboBox.removeAllItems();
        Map<String, Poll> polls = dataManager.getAllPolls();
        for (Poll poll : polls.values()) {
            pollComboBox.addItem(poll.getPollId() + " - " + poll.getTitle());
        }
    }

    private void displayPollResults() {
        tableModel.setRowCount(0);
        String selectedPollStr = (String) pollComboBox.getSelectedItem();
        if (selectedPollStr == null) return;

        String pollId = selectedPollStr.split(" - ")[0];
        Map<String, Integer> results = dataManager.getPollResults(pollId);

        int totalVotes = 0;
        for (int votes : results.values()) {
            totalVotes += votes;
        }

        for (Map.Entry<String, Integer> entry : results.entrySet()) {
            double percentage = (totalVotes == 0) ? 0 : ((double) entry.getValue() / totalVotes) * 100;
            Object[] row = {entry.getKey(), entry.getValue(), String.format("%.2f%%", percentage)};
            tableModel.addRow(row);
        }
    }
}
