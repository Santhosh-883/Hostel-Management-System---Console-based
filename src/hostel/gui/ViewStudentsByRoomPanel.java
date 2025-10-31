package hostel.gui;

import hostel.data.DataManager;
import hostel.models.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ViewStudentsByRoomPanel extends JPanel {

    private DataManager dataManager;
    private JTable studentTable;
    private DefaultTableModel tableModel;

    public ViewStudentsByRoomPanel(DataManager dataManager, Runnable onBack) {
        this.dataManager = dataManager;
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("View Students by Room", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"Room Number", "Roll Number", "Name", "Phone Number"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        studentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(studentTable);
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
                loadStudentsSortedByRoom();
            }
        });

        // Action Listener
        backButton.addActionListener(e -> onBack.run());
    }

    private void loadStudentsSortedByRoom() {
        tableModel.setRowCount(0);
        Map<String, Student> allStudents = dataManager.getAllStudents();
        List<Student> studentList = new ArrayList<>(allStudents.values());
        studentList.sort(Comparator.comparing(Student::getRoomNumber));

        for (Student student : studentList) {
            Object[] row = {student.getRoomNumber(), student.getRollNumber(), student.getName(), student.getPhoneNumber()};
            tableModel.addRow(row);
        }
    }
}
