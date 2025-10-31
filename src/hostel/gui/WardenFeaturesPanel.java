package hostel.gui;

import hostel.data.DataManager;

import javax.swing.*;
import java.awt.*;

public class WardenFeaturesPanel extends JPanel {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    public WardenFeaturesPanel(DataManager dataManager, String wardenName, Runnable onLogout) {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        setLayout(new BorderLayout());

        // --- Main Features Panel --- //
        JPanel featuresView = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("--- Warden Panel ---", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel welcomeLabel = new JLabel("Welcome, " + wardenName + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.add(titleLabel);
        titlePanel.add(welcomeLabel);
        featuresView.add(titlePanel, BorderLayout.NORTH);

        JPanel featuresPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        featuresPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton addStudentButton = new JButton("1. Add Student");
        JButton viewAllStudentsButton = new JButton("2. View All Students");
        featuresPanel.add(addStudentButton);
        featuresPanel.add(viewAllStudentsButton);
        JButton viewStudentsByRoomButton = new JButton("3. View Students by Room");
        featuresPanel.add(viewStudentsByRoomButton);
        JButton manageLeaveRequestsButton = new JButton("4. Manage Leave Requests");
        featuresPanel.add(manageLeaveRequestsButton);
        JButton viewAttendanceRecordsButton = new JButton("5. View Attendance Records");
        featuresPanel.add(viewAttendanceRecordsButton);
        JButton viewComplaintsButton = new JButton("6. View Complaints");
        featuresPanel.add(viewComplaintsButton);
        JButton dailyAttendanceReportButton = new JButton("7. Daily Attendance Report");
        featuresPanel.add(dailyAttendanceReportButton);
        JButton createPollButton = new JButton("8. Create Poll");
        featuresPanel.add(createPollButton);
        JButton viewPollResultsButton = new JButton("9. View Poll Results");
        featuresPanel.add(viewPollResultsButton);
        
        JButton logoutButton = new JButton("10. Logout");
        featuresPanel.add(logoutButton);

        featuresView.add(featuresPanel, BorderLayout.CENTER);

        // --- Add Student Panel --- //
        AddStudentPanel addStudentPanel = new AddStudentPanel(dataManager, () -> {
            cardLayout.show(mainPanel, "features");
            mainPanel.revalidate();
            mainPanel.repaint();
        });

        // --- View All Students Panel --- //
        ViewAllStudentsPanel viewAllStudentsPanel = new ViewAllStudentsPanel(dataManager, () -> {
            cardLayout.show(mainPanel, "features");
            mainPanel.revalidate();
            mainPanel.repaint();
        });

        // --- Manage Leave Requests Panel --- //
        ManageLeaveRequestsPanel manageLeaveRequestsPanel = new ManageLeaveRequestsPanel(dataManager, () -> {
            cardLayout.show(mainPanel, "features");
            mainPanel.revalidate();
            mainPanel.repaint();
        });

        // --- View Students by Room Panel --- //
        ViewStudentsByRoomPanel viewStudentsByRoomPanel = new ViewStudentsByRoomPanel(dataManager, () -> {
            cardLayout.show(mainPanel, "features");
            mainPanel.revalidate();
            mainPanel.repaint();
        });

        // --- View Attendance Records Panel --- //
        ViewAttendanceRecordsPanel viewAttendanceRecordsPanel = new ViewAttendanceRecordsPanel(dataManager, () -> {
            cardLayout.show(mainPanel, "features");
            mainPanel.revalidate();
            mainPanel.repaint();
        });

        // --- View Complaints Panel --- //
        ViewComplaintsPanel viewComplaintsPanel = new ViewComplaintsPanel(dataManager, () -> {
            cardLayout.show(mainPanel, "features");
            mainPanel.revalidate();
            mainPanel.repaint();
        });

        // --- Create Poll Panel --- //
        CreatePollPanel createPollPanel = new CreatePollPanel(dataManager, () -> {
            cardLayout.show(mainPanel, "features");
            mainPanel.revalidate();
            mainPanel.repaint();
        });

        // --- View Poll Results Panel --- //
        ViewPollResultsPanel viewPollResultsPanel = new ViewPollResultsPanel(dataManager, () -> {
            cardLayout.show(mainPanel, "features");
            mainPanel.revalidate();
            mainPanel.repaint();
        });

        // --- Daily Attendance Report Panel --- //
        DailyAttendanceReportPanel dailyAttendanceReportPanel = new DailyAttendanceReportPanel(dataManager, () -> {
            cardLayout.show(mainPanel, "features");
            mainPanel.revalidate();
            mainPanel.repaint();
        });

        mainPanel.add(featuresView, "features");
        mainPanel.add(addStudentPanel, "addStudent");
        mainPanel.add(viewAllStudentsPanel, "viewAllStudents");
        mainPanel.add(manageLeaveRequestsPanel, "manageLeaveRequests");
        mainPanel.add(viewStudentsByRoomPanel, "viewStudentsByRoom");
        mainPanel.add(viewAttendanceRecordsPanel, "viewAttendanceRecords");
        mainPanel.add(viewComplaintsPanel, "viewComplaints");
        mainPanel.add(createPollPanel, "createPoll");
        mainPanel.add(viewPollResultsPanel, "viewPollResults");
        mainPanel.add(dailyAttendanceReportPanel, "dailyAttendanceReport");

        add(mainPanel, BorderLayout.CENTER);

        // Action Listeners
        logoutButton.addActionListener(e -> onLogout.run());
        addStudentButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "addStudent");
            mainPanel.revalidate();
            mainPanel.repaint();
        });
        viewAllStudentsButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "viewAllStudents");
            mainPanel.revalidate();
            mainPanel.repaint();
        });
        manageLeaveRequestsButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "manageLeaveRequests");
            mainPanel.revalidate();
            mainPanel.repaint();
        });
        viewStudentsByRoomButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "viewStudentsByRoom");
            mainPanel.revalidate();
            mainPanel.repaint();
        });
        viewAttendanceRecordsButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "viewAttendanceRecords");
            mainPanel.revalidate();
            mainPanel.repaint();
        });
        viewComplaintsButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "viewComplaints");
            mainPanel.revalidate();
            mainPanel.repaint();
        });
        createPollButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "createPoll");
            mainPanel.revalidate();
            mainPanel.repaint();
        });
        viewPollResultsButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "viewPollResults");
            mainPanel.revalidate();
            mainPanel.repaint();
        });
        dailyAttendanceReportButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "dailyAttendanceReport");
            mainPanel.revalidate();
            mainPanel.repaint();
        });

        cardLayout.show(mainPanel, "features");
    }
}

