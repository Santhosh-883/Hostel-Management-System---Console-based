package hostel.gui;

import hostel.data.DataManager;
import hostel.models.Poll;
import hostel.models.Student;
import hostel.models.Vote;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class ParticipateInPollsPanel extends JPanel {

    private DataManager dataManager;
    private Student student;
    private JComboBox<String> pollComboBox;
    private JPanel optionsPanel;

    public ParticipateInPollsPanel(DataManager dataManager, Student student, Runnable onBack) {
        this.dataManager = dataManager;
        this.student = student;
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Participate in Polls", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // Poll Selection
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pollComboBox = new JComboBox<>();
        loadPollsIntoComboBox();
        selectionPanel.add(new JLabel("Select a Poll:"));
        selectionPanel.add(pollComboBox);
        add(selectionPanel, BorderLayout.NORTH);

        // Options Display
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(optionsPanel);
        add(scrollPane, BorderLayout.CENTER);

        // Back Button
        JButton backButton = new JButton("Back");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        pollComboBox.addActionListener(e -> displaySelectedPoll());
        backButton.addActionListener(e -> onBack.run());

        // Initial display
        displaySelectedPoll();
    }

    private void loadPollsIntoComboBox() {
        pollComboBox.removeAllItems();
        Map<String, Poll> polls = dataManager.getAllPolls();
        for (Poll poll : polls.values()) {
            pollComboBox.addItem(poll.getPollId() + " - " + poll.getTitle());
        }
    }

    private void displaySelectedPoll() {
        optionsPanel.removeAll();
        String selectedPollStr = (String) pollComboBox.getSelectedItem();
        if (selectedPollStr == null) return;

        String pollId = selectedPollStr.split(" - ")[0];
        Poll selectedPoll = dataManager.getPoll(pollId);

        if (selectedPoll != null) {
            optionsPanel.add(new JLabel("Question: " + selectedPoll.getQuestion()));

            if (dataManager.hasStudentVoted(pollId, student.getRollNumber())) {
                optionsPanel.add(new JLabel("You have already voted in this poll."));
            } else {
                ButtonGroup optionsGroup = new ButtonGroup();
                for (String option : selectedPoll.getOptions()) {
                    JRadioButton radioButton = new JRadioButton(option);
                    optionsGroup.add(radioButton);
                    optionsPanel.add(radioButton);
                }

                JButton voteButton = new JButton("Cast Vote");
                optionsPanel.add(voteButton);

                voteButton.addActionListener(e -> {
                    String selectedOption = null;
                    for (Component comp : optionsPanel.getComponents()) {
                        if (comp instanceof JRadioButton) {
                            JRadioButton button = (JRadioButton) comp;
                            if (button.isSelected()) {
                                selectedOption = button.getText();
                                break;
                            }
                        }
                    }

                    if (selectedOption != null) {
                        Vote vote = new Vote(pollId, student.getRollNumber(), selectedOption);
                        dataManager.addVote(vote);
                        JOptionPane.showMessageDialog(this, "Vote cast successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        displaySelectedPoll(); // Refresh the panel
                    } else {
                        JOptionPane.showMessageDialog(this, "Please select an option to vote.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
            }
        }

        optionsPanel.revalidate();
        optionsPanel.repaint();
    }
}
