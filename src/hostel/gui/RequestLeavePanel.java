package hostel.gui;

import hostel.data.DataManager;
import hostel.models.LeaveRequest;
import hostel.models.Student;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RequestLeavePanel extends JPanel {

    private final DataManager dataManager;
    private final Student student;

    private JComboBox<String> leaveTypeComboBox;
    private JRadioButton singleDayRadioButton;
    private com.toedter.calendar.JDateChooser fromDateChooser;
    private com.toedter.calendar.JDateChooser toDateChooser;
    private JLabel toDateLabel;
    private JLabel fromDateLabel;
    private List<JCheckBox> sessionCheckBoxes;
    private JTextField leaveReasonField;
    private JTextArea leaveDescriptionArea;
    private JLabel charCountLabel;
    private Timer timer;
    private final Calendar[] cutoffTimes = new Calendar[8];

    public RequestLeavePanel(DataManager dataManager, Student student, Runnable onBack) {
        this.dataManager = dataManager;
        this.student = student;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Title
        JLabel titleLabel = new JLabel("LEAVE REQUEST FORM", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Leave Type
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(new JLabel("LEAVE TYPE *:"), gbc);

        gbc.gridx = 1;
        leaveTypeComboBox = new JComboBox<>(new String[]{"Select", "Casual Leave", "Sick Leave", "Emergency Leave"});
        formPanel.add(leaveTypeComboBox, gbc);

        // Day
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("DAY:"), gbc);

        gbc.gridx = 1;
        singleDayRadioButton = new JRadioButton("Single", true);
        JRadioButton multiDayRadioButton = new JRadioButton("Multi");
        ButtonGroup dayGroup = new ButtonGroup();
        dayGroup.add(singleDayRadioButton);
        dayGroup.add(multiDayRadioButton);
        JPanel dayPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        dayPanel.add(singleDayRadioButton);
        dayPanel.add(multiDayRadioButton);
        formPanel.add(dayPanel, gbc);

        // Leave On
        gbc.gridx = 0;
        gbc.gridy = 2;
        fromDateLabel = new JLabel("FROM DATE *:");
        formPanel.add(fromDateLabel, gbc);

        gbc.gridx = 1;
        fromDateChooser = new com.toedter.calendar.JDateChooser();
        fromDateChooser.setDateFormatString("yyyy-MM-dd");
        // Set minimum selectable date to today
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0); // Clear time part
        fromDateChooser.setMinSelectableDate(today.getTime());

        // Set maximum selectable date to 60 days from today
        Calendar maxDate = Calendar.getInstance();
        maxDate.add(Calendar.DAY_OF_MONTH, 60);
        fromDateChooser.setMaxSelectableDate(maxDate.getTime());
        formPanel.add(fromDateChooser, gbc);

        // To Date
        gbc.gridx = 0;
        gbc.gridy = 3;
        toDateLabel = new JLabel("TO DATE *:");
        formPanel.add(toDateLabel, gbc);

        gbc.gridx = 1;
        toDateChooser = new com.toedter.calendar.JDateChooser();
        toDateChooser.setDateFormatString("yyyy-MM-dd");
        toDateChooser.setMinSelectableDate(today.getTime());
        toDateChooser.setMaxSelectableDate(maxDate.getTime());
        formPanel.add(toDateChooser, gbc);

        // Session
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("SESSION *:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JPanel sessionPanel = new JPanel(new GridLayout(0, 3, 10, 5));
        sessionCheckBoxes = new ArrayList<>();
        String[] hourLabels = {"1st Hour", "2nd Hour", "3rd Hour", "4th Hour", "5th Hour", "6th Hour", "7th Hour", "8th Hour"};
        for (String label : hourLabels) {
            JCheckBox checkBox = new JCheckBox(label);
            sessionCheckBoxes.add(checkBox);
            sessionPanel.add(checkBox);
        }
        formPanel.add(sessionPanel, gbc);

        // Leave Reason
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(new JLabel("LEAVE REASON *:"), gbc);

        gbc.gridx = 1;
        leaveReasonField = new JTextField();
        formPanel.add(leaveReasonField, gbc);

        // Leave Description
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("LEAVE DESCRIPTION *:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        leaveDescriptionArea = new JTextArea(5, 30);
        leaveDescriptionArea.setLineWrap(true);
        leaveDescriptionArea.setWrapStyleWord(true);
        JScrollPane descriptionScrollPane = new JScrollPane(leaveDescriptionArea);
        formPanel.add(descriptionScrollPane, gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.EAST;
        charCountLabel = new JLabel("You have 500 chars left");
        formPanel.add(charCountLabel, gbc);


        add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton submitButton = new JButton("Submit");
        JButton backButton = new JButton("Back");
        buttonPanel.add(submitButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        singleDayRadioButton.addActionListener(e -> toggleToDateVisibility());
        multiDayRadioButton.addActionListener(e -> toggleToDateVisibility());
        submitButton.addActionListener(e -> submitRequest());
        backButton.addActionListener(e -> onBack.run());

        // Add listener to date chooser to check if it's today
        fromDateChooser.addPropertyChangeListener("date", evt -> updateHourCheckboxes());

        initializeCutoffTimes();
        setupTimer();

        toggleToDateVisibility(); // Set initial state
        updateHourCheckboxes(); // Set initial state for checkboxes

        leaveDescriptionArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateCharCount();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateCharCount();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateCharCount();
            }
        });
    }

    private void updateCharCount() {
        int remaining = 500 - leaveDescriptionArea.getText().length();
        charCountLabel.setText("You have " + remaining + " chars left");
    }

    private void toggleToDateVisibility() {
        boolean isMultiDay = !singleDayRadioButton.isSelected();
        toDateLabel.setVisible(isMultiDay);
        toDateChooser.setVisible(isMultiDay);
        if (isMultiDay) {
            fromDateLabel.setText("FROM DATE *:");
        } else {
            fromDateLabel.setText("DATE *:");
        }
    }

    private void submitRequest() {
        // Validation
        if (leaveTypeComboBox.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please select a leave type.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Date fromDate = fromDateChooser.getDate();
        if (fromDate == null) {
            JOptionPane.showMessageDialog(this, "Please select a from date.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Date toDate = null;
        if (!singleDayRadioButton.isSelected()) {
            toDate = toDateChooser.getDate();
            if (toDate == null) {
                JOptionPane.showMessageDialog(this, "Please select a to date.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (toDate.before(fromDate)) {
                JOptionPane.showMessageDialog(this, "'To Date' must be after 'From Date'.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }


        List<String> selectedSessions = new ArrayList<>();
        for (JCheckBox checkBox : sessionCheckBoxes) {
            if (checkBox.isSelected()) {
                selectedSessions.add(checkBox.getText());
            }
        }

        if (singleDayRadioButton.isSelected() && selectedSessions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one session.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String reason = leaveReasonField.getText().trim();
        if (reason.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Leave reason cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String description = leaveDescriptionArea.getText().trim();
        if (description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Leave description cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // All validations passed, proceed to submit
        String leaveType = (String) leaveTypeComboBox.getSelectedItem();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fromDateStr = sdf.format(fromDate);
        String toDateStr = (toDate != null) ? sdf.format(toDate) : fromDateStr;
        String sessions = String.join(", ", selectedSessions);

        LeaveRequest request = new LeaveRequest(student.getRollNumber(), reason, fromDateStr, toDateStr, sessions);
        dataManager.addLeaveRequest(request);
        dataManager.addNotificationForStudent("warden", "New leave request from " + student.getRollNumber());

        JOptionPane.showMessageDialog(this, "Leave request submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        // Clear fields
        clearForm();
    }

    private void clearForm() {
        leaveTypeComboBox.setSelectedIndex(0);
        fromDateChooser.setDate(null);
        for (JCheckBox checkBox : sessionCheckBoxes) {
            checkBox.setSelected(false);
        }
        leaveReasonField.setText("");
        leaveDescriptionArea.setText("");
        singleDayRadioButton.setSelected(true);
    }

    private void initializeCutoffTimes() {
        int[] hours = {9, 10, 11, 12, 14, 15, 16, 18};
        int[] minutes = {35, 25, 35, 25, 20, 5, 15, 30};

        for (int i = 0; i < 8; i++) {
            cutoffTimes[i] = Calendar.getInstance();
            cutoffTimes[i].set(Calendar.HOUR_OF_DAY, hours[i]);
            cutoffTimes[i].set(Calendar.MINUTE, minutes[i]);
            cutoffTimes[i].set(Calendar.SECOND, 0);
            cutoffTimes[i].set(Calendar.MILLISECOND, 0);
        }
    }

    private void setupTimer() {
        timer = new Timer(60000, e -> updateHourCheckboxes()); // Check every minute
        timer.start();
    }

    private void updateHourCheckboxes() {
        Date selectedDate = fromDateChooser.getDate();
        if (selectedDate == null) {
            for (JCheckBox checkBox : sessionCheckBoxes) {
                checkBox.setEnabled(true); // Enable all if no date is selected
            }
            return;
        }

        Calendar today = Calendar.getInstance();
        Calendar selectedCal = Calendar.getInstance();
        selectedCal.setTime(selectedDate);

        boolean isToday = today.get(Calendar.YEAR) == selectedCal.get(Calendar.YEAR) &&
                        today.get(Calendar.DAY_OF_YEAR) == selectedCal.get(Calendar.DAY_OF_YEAR);

        if (isToday) {
            Calendar now = Calendar.getInstance();
            for (int i = 0; i < sessionCheckBoxes.size(); i++) {
                if (now.after(cutoffTimes[i])) {
                    sessionCheckBoxes.get(i).setEnabled(false);
                    sessionCheckBoxes.get(i).setSelected(false); // Unselect if disabled
                } else {
                    sessionCheckBoxes.get(i).setEnabled(true);
                }
            }
        } else {
            // If not today (i.e., a future date), enable all checkboxes
            for (JCheckBox checkBox : sessionCheckBoxes) {
                checkBox.setEnabled(true);
            }
        }
    }
}
