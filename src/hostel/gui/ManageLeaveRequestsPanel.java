package hostel.gui;

import hostel.data.DataManager;
import hostel.models.LeaveRequest;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManageLeaveRequestsPanel extends JPanel {

    private JTable leaveRequestsTable;
    private DefaultTableModel tableModel;
    private DataManager dataManager;

    public ManageLeaveRequestsPanel(DataManager dataManager, Runnable onBack) {
        this.dataManager = dataManager;
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Manage Leave Requests", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"Roll Number", "Reason", "Date", "Hour", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        leaveRequestsTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(leaveRequestsTable);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton approveButton = new JButton("Approve");
        JButton rejectButton = new JButton("Reject");
        JButton backButton = new JButton("Back");
        buttonPanel.add(approveButton);
        buttonPanel.add(rejectButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Reload data when the panel is shown
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                loadLeaveRequests();
            }
        });

        // Action Listeners
        approveButton.addActionListener(e -> updateRequestStatus("Approved"));
        rejectButton.addActionListener(e -> updateRequestStatus("Rejected"));
        backButton.addActionListener(e -> onBack.run());
    }

    private void loadLeaveRequests() {
        tableModel.setRowCount(0);
        List<LeaveRequest> requests = dataManager.getLeaveRequests();
        for (LeaveRequest request : requests) {
            Object[] row = {
                    request.getStudentRollNumber(),
                    request.getReason(),
                    request.getDate(),
                    request.getHour(),
                    request.getStatus()
            };
            tableModel.addRow(row);
        }
    }

    private void updateRequestStatus(String status) {
        int selectedRow = leaveRequestsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a request to update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String rollNumber = (String) tableModel.getValueAt(selectedRow, 0);
        String reason = (String) tableModel.getValueAt(selectedRow, 1);

        List<LeaveRequest> requests = dataManager.getLeaveRequests();
        for (LeaveRequest request : requests) {
            if (request.getStudentRollNumber().equals(rollNumber) && request.getReason().equals(reason)) {
                request.setStatus(status);
                break;
            }
        }

        dataManager.updateLeaveRequests(requests);
        loadLeaveRequests(); // Refresh the table
        JOptionPane.showMessageDialog(this, "Request has been " + status.toLowerCase() + ".", "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
