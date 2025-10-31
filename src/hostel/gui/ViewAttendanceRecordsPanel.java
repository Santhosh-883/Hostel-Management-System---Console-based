package hostel.gui;

import hostel.data.DataManager;
import hostel.models.Attendance;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewAttendanceRecordsPanel extends JPanel {

    private JTable attendanceTable;
    private DefaultTableModel tableModel;
    private DataManager dataManager;

    public ViewAttendanceRecordsPanel(DataManager dataManager, Runnable onBack) {
        this.dataManager = dataManager;
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("View Attendance Records", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"Roll Number", "Date", "In-Time", "Out-Time"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        attendanceTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(attendanceTable);
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
                loadAttendanceRecords();
            }
        });

        // Action Listener
        backButton.addActionListener(e -> onBack.run());
    }

    private void loadAttendanceRecords() {
        tableModel.setRowCount(0);
        List<Attendance> records = dataManager.getAttendanceRecords();
        for (Attendance record : records) {
            Object[] row = {
                    record.getStudentRollNumber(),
                    record.getDate(),
                    record.getInTime(),
                    record.getOutTime()
            };
            tableModel.addRow(row);
        }
    }
}
