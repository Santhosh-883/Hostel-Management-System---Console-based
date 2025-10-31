package hostel.gui;

import hostel.data.DataManager;
import hostel.models.Complaint;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewComplaintsPanel extends JPanel {

    private JTable complaintsTable;
    private DefaultTableModel tableModel;
    private DataManager dataManager;

    public ViewComplaintsPanel(DataManager dataManager, Runnable onBack) {
        this.dataManager = dataManager;
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("View Complaints", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"Roll Number", "Description"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        complaintsTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(complaintsTable);
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
                loadComplaints();
            }
        });

        // Action Listener
        backButton.addActionListener(e -> onBack.run());
    }

    private void loadComplaints() {
        tableModel.setRowCount(0);
        List<Complaint> complaints = dataManager.getComplaints();
        for (Complaint complaint : complaints) {
            Object[] row = {
                    complaint.getStudentRollNumber(),
                    complaint.getDescription()
            };
            tableModel.addRow(row);
        }
    }
}
