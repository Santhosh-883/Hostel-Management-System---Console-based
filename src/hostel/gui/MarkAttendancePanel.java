package hostel.gui;

import hostel.data.DataManager;
import hostel.models.Attendance;
import hostel.models.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
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
        String[] columnNames = {"Date", "In-Time", "Out-Time"};
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
        JButton markInButton = new JButton("Mark In-Time");
        JButton markOutButton = new JButton("Mark Out-Time");
        JButton backButton = new JButton("Back");
        buttonPanel.add(markInButton);
        buttonPanel.add(markOutButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadAttendanceForToday();

        // Action Listeners
        markInButton.addActionListener(e -> markInTime());
        markOutButton.addActionListener(e -> markOutTime());
        backButton.addActionListener(e -> onBack.run());
    }

    private void loadAttendanceForToday() {
        tableModel.setRowCount(0);
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        List<Attendance> allRecords = dataManager.getAttendanceRecords();
        for (Attendance record : allRecords) {
            if (record.getStudentRollNumber().equals(student.getRollNumber()) && record.getDate().equals(today)) {
                Object[] row = {record.getDate(), record.getInTime(), record.getOutTime()};
                tableModel.addRow(row);
            }
        }
    }

    private void markInTime() {
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String now = new SimpleDateFormat("HH:mm:ss").format(new Date());
        Attendance newAttendance = new Attendance(student.getRollNumber(), today, now, "Not Marked");
        dataManager.addAttendance(newAttendance);
        JOptionPane.showMessageDialog(this, "In-Time marked successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        loadAttendanceForToday();
    }

    private void markOutTime() {
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String now = new SimpleDateFormat("HH:mm:ss").format(new Date());
        List<Attendance> allRecords = dataManager.getAttendanceRecords();
        Attendance lastUnmarked = null;

        for (int i = allRecords.size() - 1; i >= 0; i--) {
            Attendance record = allRecords.get(i);
            if (record.getStudentRollNumber().equals(student.getRollNumber()) && 
                record.getDate().equals(today) && 
                "Not Marked".equals(record.getOutTime())) {
                lastUnmarked = record;
                break;
            }
        }

        if (lastUnmarked != null) {
            lastUnmarked.setOutTime(now);
            dataManager.updateAttendance(java.util.Collections.singletonList(lastUnmarked));
            JOptionPane.showMessageDialog(this, "Out-Time marked successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadAttendanceForToday();
        } else {
            JOptionPane.showMessageDialog(this, "No In-Time marked for today to mark an Out-Time against.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
