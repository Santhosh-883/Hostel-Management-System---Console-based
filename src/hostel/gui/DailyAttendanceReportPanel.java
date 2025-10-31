package hostel.gui;

import hostel.data.DataManager;
import hostel.models.Attendance;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DailyAttendanceReportPanel extends JPanel {

    private DataManager dataManager;
    private JTextField dateField;
    private JTable reportTable;
    private DefaultTableModel tableModel;

    public DailyAttendanceReportPanel(DataManager dataManager, Runnable onBack) {
        this.dataManager = dataManager;
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Daily Attendance Report", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.add(new JLabel("Enter Date (YYYY-MM-DD):"));
        dateField = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()), 10);
        searchPanel.add(dateField);
        JButton searchButton = new JButton("Generate Report");
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"Roll Number", "In-Time", "Out-Time"};
        tableModel = new DefaultTableModel(columnNames, 0);
        reportTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(reportTable);
        add(scrollPane, BorderLayout.CENTER);

        // Back Button
        JButton backButton = new JButton("Back");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        searchButton.addActionListener(e -> generateReport());
        backButton.addActionListener(e -> onBack.run());

        // Initial report for today
        generateReport();
    }

    private void generateReport() {
        String date = dateField.getText();
        if (date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a date.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        tableModel.setRowCount(0);
        List<Attendance> allRecords = dataManager.getAttendanceRecords();
        for (Attendance record : allRecords) {
            if (record.getDate().equals(date)) {
                Object[] row = {record.getStudentRollNumber(), record.getInTime(), record.getOutTime()};
                tableModel.addRow(row);
            }
        }
    }
}
