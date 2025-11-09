package hostel.data;

import hostel.models.Attendance;
import hostel.models.Complaint;
import hostel.models.LeaveRequest;
import hostel.models.Notification;
import hostel.models.Poll;
import hostel.models.Student;
import hostel.models.Vote;
import hostel.models.Warden;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager {
    private Map<String, Student> students;
    private List<LeaveRequest> leaveRequests;
    private List<Attendance> attendanceRecords;
    private List<Complaint> complaints;
    private Map<String, Warden> wardens;
    private Map<String, Poll> polls;
    private List<Vote> votes;
    private List<Notification> notifications;
    private DatabaseManager dbManager;

    public DataManager() {
        this.dbManager = DatabaseManager.getInstance();
        students = new HashMap<>();
        leaveRequests = new ArrayList<>();
        attendanceRecords = new ArrayList<>();
        complaints = new ArrayList<>();
        wardens = new HashMap<>();
        polls = new HashMap<>();
        votes = new ArrayList<>();
        notifications = new ArrayList<>();
        loadAllData();
    }

    public void addStudent(Student student) {
        String sql = "INSERT INTO students (roll_number, name, room_number, phone_number) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, student.getRollNumber());
            pstmt.setString(2, student.getName());
            pstmt.setString(3, student.getRoomNumber());
            pstmt.setString(4, student.getPhoneNumber());
            pstmt.executeUpdate();
            students.put(student.getRollNumber(), student);
        } catch (SQLException e) {
            System.err.println("Error adding student: " + e.getMessage());
        }
    }

    private void loadNotifications() {
        String sql = "SELECT id, recipient_roll_number, message, timestamp, `read` FROM notifications";
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            notifications.clear();
            while (rs.next()) {
                Notification notification = new Notification(
                        rs.getInt("id"),
                        rs.getString("recipient_roll_number"),
                        rs.getString("message"),
                        rs.getString("timestamp"),
                        rs.getBoolean(5)
                );
                notifications.add(notification);
            }
        } catch (SQLException e) {
            System.err.println("Error loading notifications: " + e.getMessage());
        }
    }

    public Student getStudent(String rollNumber) {
        return students.get(rollNumber);
    }

    public Map<String, Student> getAllStudents() {
        return students;
    }

    public void addLeaveRequest(LeaveRequest request) {
        String sql = "INSERT INTO leave_requests (student_roll_number, reason, from_date, to_date, hour, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, request.getStudentRollNumber());
            pstmt.setString(2, request.getReason());
            pstmt.setString(3, request.getFromDate());
            pstmt.setString(4, request.getToDate());
            pstmt.setString(5, request.getHour());
            pstmt.setString(6, request.getStatus());
            pstmt.executeUpdate();
            leaveRequests.add(request);
        } catch (SQLException e) {
            System.err.println("Error adding leave request: " + e.getMessage());
        }
    }

    public List<LeaveRequest> getLeaveRequests() {
        return leaveRequests;
    }

    public void updateLeaveRequests(List<LeaveRequest> requests) {
        String sql = "UPDATE leave_requests SET status = ? WHERE student_roll_number = ? AND reason = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (LeaveRequest request : requests) {
                if (!request.getStatus().equals("Pending")) {
                    pstmt.setString(1, request.getStatus());
                    pstmt.setString(2, request.getStudentRollNumber());
                    pstmt.setString(3, request.getReason());
                    pstmt.executeUpdate();
                }
            }
            // Reload data from database to reflect changes
            loadLeaveRequests();
        } catch (SQLException e) {
            System.err.println("Error updating leave requests: " + e.getMessage());
        }
    }

    public void addAttendance(Attendance attendance) {
        String sql = "INSERT INTO attendance (student_roll_number, date, in_time, out_time) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, attendance.getStudentRollNumber());
            pstmt.setString(2, attendance.getDate());
            pstmt.setString(3, attendance.getInTime());
            pstmt.setString(4, attendance.getOutTime());
            pstmt.executeUpdate();
            attendanceRecords.add(attendance);
        } catch (SQLException e) {
            System.err.println("Error adding attendance: " + e.getMessage());
        }
    }

    public List<Attendance> getAttendanceRecords() {
        return attendanceRecords;
    }

    public void updateAttendance(List<Attendance> records) {
        String sql = "UPDATE attendance SET out_time = ? WHERE student_roll_number = ? AND date = ? AND in_time = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (Attendance record : records) {
                pstmt.setString(1, record.getOutTime());
                pstmt.setString(2, record.getStudentRollNumber());
                pstmt.setString(3, record.getDate());
                pstmt.setString(4, record.getInTime());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            // Reload data from database to reflect changes
            loadAttendance();
        } catch (SQLException e) {
            System.err.println("Error updating attendance: " + e.getMessage());
        }
    }

    public void addComplaint(Complaint complaint) {
        String sql = "INSERT INTO complaints (student_roll_number, description) VALUES (?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, complaint.getStudentRollNumber());
            pstmt.setString(2, complaint.getDescription());
            pstmt.executeUpdate();
            complaints.add(complaint);
        } catch (SQLException e) {
            System.err.println("Error adding complaint: " + e.getMessage());
        }
    }

    public List<Complaint> getComplaints() {
        return complaints;
    }

    public void addWarden(Warden warden) {
        String sql = "INSERT INTO wardens (username, name, password) VALUES (?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, warden.getUsername());
            pstmt.setString(2, warden.getName());
            pstmt.setString(3, warden.getPassword());
            pstmt.executeUpdate();
            wardens.put(warden.getUsername(), warden);
        } catch (SQLException e) {
            System.err.println("Error adding warden: " + e.getMessage());
        }
    }

    public Warden getWarden(String username) {
        return wardens.get(username);
    }

    public boolean authenticateWarden(String username, String password) {
        Warden warden = wardens.get(username);
        return warden != null && warden.getPassword().equals(password);
    }

    public Map<String, Warden> getAllWardens() {
        return wardens;
    }

    public void addPoll(Poll poll) {
        String sql = "INSERT INTO polls (poll_id, title, question, options, creator_warden) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, poll.getPollId());
            pstmt.setString(2, poll.getTitle());
            pstmt.setString(3, poll.getQuestion());
            pstmt.setString(4, String.join(",", poll.getOptions()));
            pstmt.setString(5, poll.getCreatorWarden());
            pstmt.executeUpdate();
            polls.put(poll.getPollId(), poll);
        } catch (SQLException e) {
            System.err.println("Error adding poll: " + e.getMessage());
        }
    }

    public Poll getPoll(String pollId) {
        return polls.get(pollId);
    }

    public Map<String, Poll> getAllPolls() {
        return polls;
    }

    public void addNotification(Notification notification) {
        String sql = "INSERT INTO notifications (recipient_roll_number, message, timestamp, `read`) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, notification.getRecipientRollNumber());
            pstmt.setString(2, notification.getMessage());
            pstmt.setString(3, notification.getTimestamp());
            pstmt.setInt(4, notification.isRead() ? 1 : 0);
            pstmt.executeUpdate();
            loadNotifications();
        } catch (SQLException e) {
            System.err.println("Error adding notification: " + e.getMessage());
        }
    }

    public void addNotificationForStudent(String rollNumber, String message) {
        addNotification(new Notification(rollNumber, message));
    }

    public void addNotificationForAllStudents(String message) {
        for (String rollNumber : students.keySet()) {
            addNotificationForStudent(rollNumber, message);
        }
    }

    public List<Notification> getNotificationsForStudent(String rollNumber) {
        List<Notification> studentNotifications = new ArrayList<>();
        for (Notification notification : notifications) {
            if (notification.getRecipientRollNumber().equals(rollNumber)) {
                studentNotifications.add(notification);
            }
        }
        studentNotifications.sort((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()));
        return studentNotifications;
    }

    public long getUnreadNotificationCount(String rollNumber) {
        return notifications.stream()
                .filter(n -> n.getRecipientRollNumber().equals(rollNumber) && !n.isRead())
                .count();
    }

    public void markNotificationAsRead(int notificationId) {
        String sql = "UPDATE notifications SET `read` = 1 WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, notificationId);
            pstmt.executeUpdate();
            loadNotifications();
        } catch (SQLException e) {
            System.err.println("Error marking notification as read: " + e.getMessage());
        }
    }

    public void clearNotificationsForStudent(String rollNumber) {
        String sql = "DELETE FROM notifications WHERE recipient_roll_number = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, rollNumber);
            pstmt.executeUpdate();
            loadNotifications();
        } catch (SQLException e) {
            System.err.println("Error clearing notifications: " + e.getMessage());
        }
    }

    public void saveRememberMe(String userType, String identifier) {
        String sql = "INSERT OR REPLACE INTO remember_me (user_type, identifier) VALUES (?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userType);
            pstmt.setString(2, identifier);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving remember me data: " + e.getMessage());
        }
    }

    public void clearRememberMe(String userType) {
        String sql = "DELETE FROM remember_me WHERE user_type = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userType);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error clearing remember me data: " + e.getMessage());
        }
    }

    public String getRememberedIdentifier(String userType) {
        String sql = "SELECT identifier FROM remember_me WHERE user_type = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userType);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("identifier");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving remember me data: " + e.getMessage());
        }
        return null;
    }

    public void addVote(Vote vote) {
        String sql = "INSERT INTO votes (poll_id, student_roll_number, selected_option) VALUES (?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, vote.getPollId());
            pstmt.setString(2, vote.getStudentRollNumber());
            pstmt.setString(3, vote.getSelectedOption());
            pstmt.executeUpdate();
            votes.add(vote);
        } catch (SQLException e) {
            System.err.println("Error adding vote: " + e.getMessage());
        }
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public List<Vote> getVotesForPoll(String pollId) {
        List<Vote> pollVotes = new ArrayList<>();
        for (Vote vote : votes) {
            if (vote.getPollId().equals(pollId)) {
                pollVotes.add(vote);
            }
        }
        return pollVotes;
    }

    public boolean hasStudentVoted(String pollId, String studentRollNumber) {
        for (Vote vote : votes) {
            if (vote.getPollId().equals(pollId) && vote.getStudentRollNumber().equals(studentRollNumber)) {
                return true;
            }
        }
        return false;
    }

    public Map<String, Integer> getPollResults(String pollId) {
        Map<String, Integer> results = new HashMap<>();
        Poll poll = polls.get(pollId);
        if (poll != null) {
            for (String option : poll.getOptions()) {
                results.put(option, 0);
            }
            for (Vote vote : votes) {
                if (vote.getPollId().equals(pollId)) {
                    String option = vote.getSelectedOption();
                    results.put(option, results.get(option) + 1);
                }
            }
        }
        return results;
    }

    private void loadAllData() {
        loadStudents();
        loadWardens();
        loadComplaints();
        loadLeaveRequests();
        loadAttendance();
        loadPolls();
        loadVotes();
        loadNotifications();
    }

    private void loadStudents() {
        String sql = "SELECT roll_number, name, room_number, phone_number FROM students";
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Student student = new Student(
                    rs.getString("name"),
                    rs.getString("roll_number"),
                    rs.getString("room_number"),
                    rs.getString("phone_number")
                );
                students.put(student.getRollNumber(), student);
            }
        } catch (SQLException e) {
            System.err.println("Error loading students: " + e.getMessage());
        }
    }

    private void loadWardens() {
        String sql = "SELECT username, name, password FROM wardens";
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Warden warden = new Warden(
                    rs.getString("name"),
                    rs.getString("username"),
                    rs.getString("password")
                );
                wardens.put(warden.getUsername(), warden);
            }
        } catch (SQLException e) {
            System.err.println("Error loading wardens: " + e.getMessage());
        }
    }

    private void loadComplaints() {
        String sql = "SELECT student_roll_number, description FROM complaints";
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Complaint complaint = new Complaint(
                    rs.getString("student_roll_number"),
                    rs.getString("description")
                );
                complaints.add(complaint);
            }
        } catch (SQLException e) {
            System.err.println("Error loading complaints: " + e.getMessage());
        }
    }

    private void loadLeaveRequests() {
        String sql = "SELECT student_roll_number, reason, from_date, to_date, hour, status FROM leave_requests";
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            leaveRequests.clear(); // Clear the list before loading
            while (rs.next()) {
                LeaveRequest request = new LeaveRequest(
                    rs.getString("student_roll_number"),
                    rs.getString("reason"),
                    rs.getString("from_date"),
                    rs.getString("to_date"),
                    rs.getString("hour"),
                    rs.getString("status")
                );
                leaveRequests.add(request);
            }
        } catch (SQLException e) {
            System.err.println("Error loading leave requests: " + e.getMessage());
        }
    }

    private void loadAttendance() {
        String sql = "SELECT student_roll_number, date, in_time, out_time FROM attendance";
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            attendanceRecords.clear();
            while (rs.next()) {
                Attendance attendance = new Attendance(
                    rs.getString("student_roll_number"),
                    rs.getString("date"),
                    rs.getString("in_time"),
                    rs.getString("out_time")
                );
                attendanceRecords.add(attendance);
            }
        } catch (SQLException e) {
            System.err.println("Error loading attendance: " + e.getMessage());
        }
    }

    private void loadPolls() {
        String sql = "SELECT poll_id, title, question, options, creator_warden FROM polls";
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String optionsStr = rs.getString("options");
                List<String> options = new ArrayList<>();
                for (String option : optionsStr.split(",")) {
                    options.add(option.trim());
                }
                Poll poll = new Poll(
                    rs.getString("poll_id"),
                    rs.getString("title"),
                    rs.getString("question"),
                    options,
                    rs.getString("creator_warden")
                );
                polls.put(poll.getPollId(), poll);
            }
        } catch (SQLException e) {
            System.err.println("Error loading polls: " + e.getMessage());
        }
    }

    private void loadVotes() {
        String sql = "SELECT poll_id, student_roll_number, selected_option FROM votes";
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Vote vote = new Vote(
                    rs.getString("poll_id"),
                    rs.getString("student_roll_number"),
                    rs.getString("selected_option")
                );
                votes.add(vote);
            }
        } catch (SQLException e) {
            System.err.println("Error loading votes: " + e.getMessage());
        }
    }
}
