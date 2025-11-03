package hostel.gui;

import hostel.data.DataManager;
import hostel.models.Attendance;
import hostel.models.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MarkAttendancePanel extends JPanel {

    private DataManager dataManager;
    private Student student;
    private JTable attendanceTable;
    private DefaultTableModel tableModel;

    public MarkAttendancePanel(DataManager dataManager, Student student, Runnable onBack) {
        this.dataManager = dataManager;
        this.student = student;
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Mark Attendance", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"Date", "Out-Time", "In-Time"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        attendanceTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton markOutButton = new JButton("Mark Out-Time");
        JButton markInButton = new JButton("Mark In-Time");
        JButton backButton = new JButton("Back");
        buttonPanel.add(markOutButton);
        buttonPanel.add(markInButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadAttendanceForToday();

        // Action Listeners
        markOutButton.addActionListener(e -> markOutTime());
        markInButton.addActionListener(e -> markInTime());
        backButton.addActionListener(e -> onBack.run());
    }

    private void loadAttendanceForToday() {
        tableModel.setRowCount(0);
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        List<Attendance> allRecords = dataManager.getAttendanceRecords();
        for (Attendance record : allRecords) {
            if (record.getStudentRollNumber().equals(student.getRollNumber()) && record.getDate().equals(today)) {
                Object[] row = {record.getDate(), record.getOutTime(), record.getInTime()};
                tableModel.addRow(row);
            }
        }
    }

    private void markOutTime() {
        // Time restriction validation
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        double currentTime = hour + minute / 60.0;

        boolean isMorningSession = (currentTime >= 7.5 && currentTime <= 8.75); // 7:30 to 8:45
        boolean isEveningSession = (currentTime >= 16.25 && currentTime <= 19.25); // 4:15 to 7:15

        if (!isMorningSession && !isEveningSession) {
            JOptionPane.showMessageDialog(this, "You can only mark Out-Time between 7:30-8:45 AM and 4:15-7:15 PM.", "Time Restriction", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if already marked out
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        List<Attendance> allRecords = dataManager.getAttendanceRecords();
        for (Attendance record : allRecords) {
            if (record.getStudentRollNumber().equals(student.getRollNumber()) &&
                record.getDate().equals(today) &&
                "Not Marked".equals(record.getInTime())) {
                JOptionPane.showMessageDialog(this, "You have already marked out. Please mark in before marking out again.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Proceed to mark out
        String now = new SimpleDateFormat("HH:mm:ss").format(new Date());
        Attendance newAttendance = new Attendance(student.getRollNumber(), today, "Not Marked", now);
        dataManager.addAttendance(newAttendance);
        JOptionPane.showMessageDialog(this, "Out-Time marked successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        loadAttendanceForToday();
    }

    private void markInTime() {
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String now = new SimpleDateFormat("HH:mm:ss").format(new Date());
        List<Attendance> allRecords = dataManager.getAttendanceRecords();
        Attendance lastUnmarked = null;

        // Find the latest record for today that has an out-time but no in-time
        for (int i = allRecords.size() - 1; i >= 0; i--) {
            Attendance record = allRecords.get(i);
            if (record.getStudentRollNumber().equals(student.getRollNumber()) &&
                record.getDate().equals(today) &&
                "Not Marked".equals(record.getInTime())) {
                lastUnmarked = record;
                break;
            }
        }

        if (lastUnmarked != null) {
            lastUnmarked.setInTime(now);
            dataManager.updateAttendance(java.util.Collections.singletonList(lastUnmarked)); // Assuming updateAttendance takes a list
            JOptionPane.showMessageDialog(this, "In-Time marked successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadAttendanceForToday();
        } else {
            JOptionPane.showMessageDialog(this, "You must mark out before you can mark in.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
