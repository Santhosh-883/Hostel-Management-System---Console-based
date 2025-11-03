package hostel.gui;

import hostel.data.DataManager;

import javax.swing.*;
import java.awt.*;

public class WardenFeaturesPanel extends JPanel {

    private final DataManager dataManager;
    private final JButton notificationsButton;
    private final WardenNotificationsPanel notificationsPanel;
    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    public WardenFeaturesPanel(DataManager dataManager, String wardenName, Runnable onLogout) {
        this.dataManager = dataManager;
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        setLayout(new BorderLayout());

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

        notificationsButton = new JButton();
        featuresPanel.add(notificationsButton);

        JButton logoutButton = new JButton("10. Logout");
        featuresPanel.add(logoutButton);

        featuresView.add(featuresPanel, BorderLayout.CENTER);

        // Panels for each feature
        AddStudentPanel addStudentPanel = new AddStudentPanel(dataManager, this::showFeatures);
        ViewAllStudentsPanel viewAllStudentsPanel = new ViewAllStudentsPanel(dataManager, this::showFeatures);
        ViewStudentsByRoomPanel viewStudentsByRoomPanel = new ViewStudentsByRoomPanel(dataManager, this::showFeatures);
        ManageLeaveRequestsPanel manageLeaveRequestsPanel = new ManageLeaveRequestsPanel(dataManager, this::showFeatures);
        ViewAttendanceRecordsPanel viewAttendanceRecordsPanel = new ViewAttendanceRecordsPanel(dataManager, this::showFeatures);
        ViewComplaintsPanel viewComplaintsPanel = new ViewComplaintsPanel(dataManager, this::showFeatures);
        DailyAttendanceReportPanel dailyAttendanceReportPanel = new DailyAttendanceReportPanel(dataManager, this::showFeatures);
        CreatePollPanel createPollPanel = new CreatePollPanel(dataManager, this::showFeatures);
        ViewPollResultsPanel viewPollResultsPanel = new ViewPollResultsPanel(dataManager, this::showFeatures);
        notificationsPanel = new WardenNotificationsPanel(dataManager, this::showFeatures);

        mainPanel.add(featuresView, "features");
        mainPanel.add(addStudentPanel, "addStudent");
        mainPanel.add(viewAllStudentsPanel, "viewAllStudents");
        mainPanel.add(viewStudentsByRoomPanel, "viewStudentsByRoom");
        mainPanel.add(manageLeaveRequestsPanel, "manageLeaveRequests");
        mainPanel.add(viewAttendanceRecordsPanel, "viewAttendanceRecords");
        mainPanel.add(viewComplaintsPanel, "viewComplaints");
        mainPanel.add(dailyAttendanceReportPanel, "dailyAttendanceReport");
        mainPanel.add(createPollPanel, "createPoll");
        mainPanel.add(viewPollResultsPanel, "viewPollResults");
        mainPanel.add(notificationsPanel, "notifications");

        add(mainPanel, BorderLayout.CENTER);

        // Action Listeners
        addStudentButton.addActionListener(e -> showPanel("addStudent"));
        viewAllStudentsButton.addActionListener(e -> showPanel("viewAllStudents"));
        viewStudentsByRoomButton.addActionListener(e -> showPanel("viewStudentsByRoom"));
        manageLeaveRequestsButton.addActionListener(e -> showPanel("manageLeaveRequests"));
        viewAttendanceRecordsButton.addActionListener(e -> showPanel("viewAttendanceRecords"));
        viewComplaintsButton.addActionListener(e -> showPanel("viewComplaints"));
        dailyAttendanceReportButton.addActionListener(e -> showPanel("dailyAttendanceReport"));
        createPollButton.addActionListener(e -> showPanel("createPoll"));
        viewPollResultsButton.addActionListener(e -> showPanel("viewPollResults"));
        notificationsButton.addActionListener(e -> {
            notificationsPanel.refreshNotifications();
            showPanel("notifications");
        });
        logoutButton.addActionListener(e -> onLogout.run());

        showFeatures();
    }

    private void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }

    private void showFeatures() {
        updateNotificationsButtonText();
        cardLayout.show(mainPanel, "features");
    }

    private void updateNotificationsButtonText() {
        long unread = dataManager.getUnreadNotificationCount("warden");
        if (unread > 0) {
            notificationsButton.setText("10. Notifications (" + unread + " new)");
        } else {
            notificationsButton.setText("10. Notifications");
        }
    }
}
