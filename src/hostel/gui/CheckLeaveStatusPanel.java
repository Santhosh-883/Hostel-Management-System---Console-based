package hostel.gui;

import hostel.data.DataManager;
import hostel.models.LeaveRequest;
import hostel.models.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CheckLeaveStatusPanel extends JPanel {

    private JTable leaveStatusTable;
    private DefaultTableModel tableModel;
    private DataManager dataManager;
    private Student student;

    public CheckLeaveStatusPanel(DataManager dataManager, Student student, Runnable onBack) {
        this.dataManager = dataManager;
        this.student = student;
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Check Leave Status", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"Reason", "From Date", "To Date", "Hour", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        leaveStatusTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(leaveStatusTable);
        add(scrollPane, BorderLayout.CENTER);

        // Back Button
        JButton backButton = new JButton("Back");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Reload data when the panel is shown
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                loadLeaveStatus();
            }
        });

        // Action Listener
        backButton.addActionListener(e -> onBack.run());
    }

    private void loadLeaveStatus() {
        tableModel.setRowCount(0);
        List<LeaveRequest> allRequests = dataManager.getLeaveRequests();
        for (LeaveRequest request : allRequests) {
            if (request.getStudentRollNumber().equals(student.getRollNumber())) {
                Object[] row = {
                        request.getReason(),
                        request.getFromDate(),
                        request.getToDate(),
                        request.getHour(),
                        request.getStatus()
                };
                tableModel.addRow(row);
            }
        }
    }
}
