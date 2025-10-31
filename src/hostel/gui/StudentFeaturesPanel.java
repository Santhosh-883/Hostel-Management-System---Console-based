package hostel.gui;

import hostel.data.DataManager;
import hostel.models.Student;

import javax.swing.*;
import java.awt.*;

public class StudentFeaturesPanel extends JPanel {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    public StudentFeaturesPanel(DataManager dataManager, Student student, Runnable onLogout) {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        setLayout(new BorderLayout());

        // --- Main Features Panel --- //
        JPanel featuresView = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("--- Student Panel ---", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel welcomeLabel = new JLabel("Welcome, " + student.getName() + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.add(titleLabel);
        titlePanel.add(welcomeLabel);
        featuresView.add(titlePanel, BorderLayout.NORTH);

        JPanel featuresPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        featuresPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton viewDetailsButton = new JButton("1. View Personal Details");
        JButton requestLeaveButton = new JButton("2. Request Leave");
        featuresPanel.add(viewDetailsButton);
        featuresPanel.add(requestLeaveButton);
        JButton checkLeaveStatusButton = new JButton("3. Check Leave Status");
        featuresPanel.add(checkLeaveStatusButton);
        JButton markAttendanceButton = new JButton("4. Mark Attendance");
        featuresPanel.add(markAttendanceButton);
        JButton raiseComplaintButton = new JButton("5. Raise Complaint");
        featuresPanel.add(raiseComplaintButton);
        JButton participateInPollsButton = new JButton("6. Participate in Polls");
        featuresPanel.add(participateInPollsButton);

        JButton logoutButton = new JButton("7. Logout");
        featuresPanel.add(logoutButton);

        featuresView.add(featuresPanel, BorderLayout.CENTER);

        // --- Request Leave Panel --- //
        RequestLeavePanel requestLeavePanel = new RequestLeavePanel(dataManager, student, () -> {
            cardLayout.show(mainPanel, "features");
            mainPanel.revalidate();
            mainPanel.repaint();
        });

        // --- View Personal Details Panel --- //
        ViewPersonalDetailsPanel viewDetailsPanel = new ViewPersonalDetailsPanel(student, () -> {
            cardLayout.show(mainPanel, "features");
            mainPanel.revalidate();
            mainPanel.repaint();
        });

        // --- Check Leave Status Panel --- //
        CheckLeaveStatusPanel checkLeaveStatusPanel = new CheckLeaveStatusPanel(dataManager, student, () -> {
            cardLayout.show(mainPanel, "features");
            mainPanel.revalidate();
            mainPanel.repaint();
        });

        // --- Mark Attendance Panel --- //
        MarkAttendancePanel markAttendancePanel = new MarkAttendancePanel(dataManager, student, () -> {
            cardLayout.show(mainPanel, "features");
            mainPanel.revalidate();
            mainPanel.repaint();
        });

        // --- Raise Complaint Panel --- //
        RaiseComplaintPanel raiseComplaintPanel = new RaiseComplaintPanel(dataManager, student, () -> {
            cardLayout.show(mainPanel, "features");
            mainPanel.revalidate();
            mainPanel.repaint();
        });

        // --- Participate in Polls Panel --- //
        ParticipateInPollsPanel participateInPollsPanel = new ParticipateInPollsPanel(dataManager, student, () -> {
            cardLayout.show(mainPanel, "features");
            mainPanel.revalidate();
            mainPanel.repaint();
        });

        mainPanel.add(featuresView, "features");
        mainPanel.add(requestLeavePanel, "requestLeave");
        mainPanel.add(viewDetailsPanel, "viewDetails");
        mainPanel.add(checkLeaveStatusPanel, "checkLeaveStatus");
        mainPanel.add(markAttendancePanel, "markAttendance");
        mainPanel.add(raiseComplaintPanel, "raiseComplaint");
        mainPanel.add(participateInPollsPanel, "participateInPolls");

        add(mainPanel, BorderLayout.CENTER);

        // Action Listeners
        logoutButton.addActionListener(e -> onLogout.run());
        requestLeaveButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "requestLeave");
            mainPanel.revalidate();
            mainPanel.repaint();
        });
        viewDetailsButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "viewDetails");
            mainPanel.revalidate();
            mainPanel.repaint();
        });
        checkLeaveStatusButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "checkLeaveStatus");
            mainPanel.revalidate();
            mainPanel.repaint();
        });
        markAttendanceButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "markAttendance");
            mainPanel.revalidate();
            mainPanel.repaint();
        });
        raiseComplaintButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "raiseComplaint");
            mainPanel.revalidate();
            mainPanel.repaint();
        });
        participateInPollsButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "participateInPolls");
            mainPanel.revalidate();
            mainPanel.repaint();
        });

        cardLayout.show(mainPanel, "features");
    }
}

