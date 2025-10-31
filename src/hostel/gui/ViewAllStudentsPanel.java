package hostel.gui;

import hostel.data.DataManager;
import hostel.models.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

public class ViewAllStudentsPanel extends JPanel {

    private JTable studentTable;
    private DefaultTableModel tableModel;
    private DataManager dataManager;
    private JTextField searchField;

    public ViewAllStudentsPanel(DataManager dataManager, Runnable onBack) {
        this.dataManager = dataManager;
        setLayout(new BorderLayout());

        // Top panel with title and search bar
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("View All Students", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.add(new JLabel("Search by Roll Number:"));
        searchField = new JTextField(20);
        searchPanel.add(searchField);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"Roll Number", "Name", "Room Number", "Phone Number"};
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
                loadStudentData("");
            }
        });

        // Action Listeners
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterStudents(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterStudents(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterStudents(); }

            private void filterStudents() {
                loadStudentData(searchField.getText());
            }
        });
        backButton.addActionListener(e -> onBack.run());
    }

    private void loadStudentData(String filter) {
        tableModel.setRowCount(0);
        Map<String, Student> students = dataManager.getAllStudents();
        for (Student student : students.values()) {
            if (student.getRollNumber().toLowerCase().contains(filter.toLowerCase())) {
                Object[] row = {
                        student.getRollNumber(),
                        student.getName(),
                        student.getRoomNumber(),
                        student.getPhoneNumber()
                };
                tableModel.addRow(row);
            }
        }
    }
}
