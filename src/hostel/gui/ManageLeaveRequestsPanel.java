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
        String[] columnNames = {"Roll Number", "Reason", "From Date", "To Date", "Hour", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        leaveRequestsTable = new JTable(tableModel) {
            @Override
            public String getToolTipText(java.awt.event.MouseEvent e) {
                int row = rowAtPoint(e.getPoint());
                int col = columnAtPoint(e.getPoint());
                if (row >= 0 && col == 4) { // Hour column
                    return "Click to view full hours";
                }
                return null;
            }
        };

        // Add mouse listener to handle hour cell clicks
        leaveRequestsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = leaveRequestsTable.rowAtPoint(e.getPoint());
                int col = leaveRequestsTable.columnAtPoint(e.getPoint());
                if (row >= 0 && col == 4) { // Hour column
                    // Get the roll number to find the original request
                    String rollNumber = (String) tableModel.getValueAt(row, 0);
                    String fromDate = (String) tableModel.getValueAt(row, 2);
                    String toDate = (String) tableModel.getValueAt(row, 3);
                    
                    // Find the original request to get the full hours
                    for (LeaveRequest request : dataManager.getLeaveRequests()) {
                        if (request.getStudentRollNumber().equals(rollNumber) &&
                            request.getFromDate().equals(fromDate) &&
                            request.getToDate().equals(toDate)) {
                            
                            // Format the hours for display
                            String fullHours = request.getHour();
                            if (fullHours != null) {
                                fullHours = fullHours.replace("Hour ", "");
                                fullHours = fullHours.replace(", ", ",");
                                fullHours = String.join(", ", fullHours.split(","));
                                
                                JOptionPane.showMessageDialog(ManageLeaveRequestsPanel.this, 
                                    "<html><body><h3>Leave Request Details</h3>" +
                                    "<p><b>Student:</b> " + rollNumber + "</p>" +
                                    "<p><b>From:</b> " + fromDate + "</p>" +
                                    "<p><b>To:</b> " + toDate + "</p>" +
                                    "<p><b>Hours:</b><br>" + fullHours.replace(",", "<br>") + "</p>" +
                                    "</body></html>", 
                                    "Leave Request Details", 
                                    JOptionPane.INFORMATION_MESSAGE);
                            }
                            break;
                        }
                    }
                }
            }
        });

        // Adjust column widths
        leaveRequestsTable.getColumnModel().getColumn(0).setPreferredWidth(100); // Roll Number
        leaveRequestsTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Reason
        leaveRequestsTable.getColumnModel().getColumn(2).setPreferredWidth(100); // From Date
        leaveRequestsTable.getColumnModel().getColumn(3).setPreferredWidth(100); // To Date
        leaveRequestsTable.getColumnModel().getColumn(4).setPreferredWidth(250); // Hour
        leaveRequestsTable.getColumnModel().getColumn(5).setPreferredWidth(80);  // Status

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
            // Format hours for display
            String hourDisplay = request.getHour();
            if (hourDisplay != null) {
                // Clean up the hour string (e.g., "Hour 1, Hour 2" -> "1,2")
                hourDisplay = hourDisplay.replace("Hour ", "").replace(" ", "");
                // Truncate if too long
                if (hourDisplay.length() > 20) {
                    hourDisplay = hourDisplay.substring(0, 17) + "...";
                }
            } else {
                hourDisplay = "";
            }
            
            Object[] row = {
                request.getStudentRollNumber(),
                request.getReason(),
                request.getFromDate(),
                request.getToDate(),
                hourDisplay,
                request.getStatus()
            };
            tableModel.addRow(row);
        }
    }

    private void updateRequestStatus(String newStatus) {
        int selectedRow = leaveRequestsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a request to update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String rollNumber = (String) tableModel.getValueAt(selectedRow, 0);
        String reason = (String) tableModel.getValueAt(selectedRow, 1);
        String currentStatus = (String) tableModel.getValueAt(selectedRow, 5);

        if (!currentStatus.equals("Pending")) {
            JOptionPane.showMessageDialog(this, "This request has already been " + currentStatus.toLowerCase() + ".", "Action Not Allowed", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<LeaveRequest> requests = dataManager.getLeaveRequests();
        LeaveRequest targetRequest = null;
        for (LeaveRequest request : requests) {
            if (request.getStudentRollNumber().equals(rollNumber) && request.getReason().equals(reason)) {
                request.setStatus(newStatus);
                targetRequest = request;
                break;
            }
        }

        dataManager.updateLeaveRequests(requests);
        if (targetRequest != null) {
            String message = String.format("Your leave request for %s (%s) was %s.",
                    targetRequest.getFromDate(),
                    targetRequest.getReason(),
                    newStatus.toLowerCase());
            dataManager.addNotificationForStudent(rollNumber, message);
        }
        loadLeaveRequests(); // Refresh the table
        JOptionPane.showMessageDialog(this, "Request has been " + newStatus.toLowerCase() + ".", "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
