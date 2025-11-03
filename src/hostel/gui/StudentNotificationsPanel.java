package hostel.gui;

import hostel.data.DataManager;
import hostel.models.Notification;
import hostel.models.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentNotificationsPanel extends JPanel {

    private final DataManager dataManager;
    private final Student student;
    private final DefaultTableModel tableModel;
    private final Runnable onBack;
    private final Runnable onNotificationsChanged;

    public StudentNotificationsPanel(DataManager dataManager, Student student, Runnable onBack, Runnable onNotificationsChanged) {
        this.dataManager = dataManager;
        this.student = student;
        this.onBack = onBack;
        this.onNotificationsChanged = onNotificationsChanged;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("Notifications", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Timestamp", "Message", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel) {
            @Override
            public String getToolTipText(java.awt.event.MouseEvent e) {
                int row = rowAtPoint(e.getPoint());
                int col = columnAtPoint(e.getPoint());
                if (row >= 0 && col == 2) { // Message column
                    return getValueAt(row, col).toString();
                }
                return null;
            }
        };
        
        // Hide ID column
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);
        
        // Add mouse listener to show full message when clicked
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (row >= 0 && col == 2) { // Message column
                    // Get the notification ID from the hidden column
                    int notificationId = (int) table.getValueAt(row, 0);
                    // Find the original notification to get the full message
                    for (Notification n : dataManager.getNotificationsForStudent(student.getRollNumber())) {
                        if (n.getId() == notificationId) {
                            JOptionPane.showMessageDialog(StudentNotificationsPanel.this, 
                                n.getMessage(), "Full Message", JOptionPane.INFORMATION_MESSAGE);
                            break;
                        }
                    }
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton markReadButton = new JButton("Mark as Read");
        JButton refreshButton = new JButton("Refresh");
        JButton clearButton = new JButton("Clear All");
        JButton backButton = new JButton("Back");
        buttonPanel.add(refreshButton);
        buttonPanel.add(markReadButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshButton.addActionListener(e -> refreshNotifications());
        markReadButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Select a notification to mark as read.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int notificationId = (int) tableModel.getValueAt(selectedRow, 0);
            dataManager.markNotificationAsRead(notificationId);
            refreshNotifications();
        });

        clearButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Clear all notifications?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dataManager.clearNotificationsForStudent(student.getRollNumber());
                refreshNotifications();
            }
        });

        backButton.addActionListener(e -> this.onBack.run());

        refreshNotifications();
    }

    public void refreshNotifications() {
        tableModel.setRowCount(0);
        List<Notification> notifications = dataManager.getNotificationsForStudent(student.getRollNumber());
        for (Notification notification : notifications) {
            // Truncate message for display
            String message = notification.getMessage();
            String displayMessage = (message.length() > 30) ? 
                message.substring(0, 27) + "..." : message;
                
            Object[] row = {
                notification.getId(),
                notification.getTimestamp(),
                displayMessage,
                notification.isRead() ? "Read" : "Unread"
            };
            tableModel.addRow(row);
        }
        if (onNotificationsChanged != null) {
            onNotificationsChanged.run();
        }
    }
}
