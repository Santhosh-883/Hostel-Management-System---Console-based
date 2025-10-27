package hostel.data;

import hostel.models.Attendance;
import hostel.models.Complaint;
import hostel.models.LeaveRequest;
import hostel.models.Poll;
import hostel.models.Student;
import hostel.models.Vote;
import hostel.models.Warden;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class DataManager {
    private static final String STUDENTS_FILE = "../data/students.txt";
    private static final String LEAVE_REQUESTS_FILE = "../data/leave_requests.txt";
    private static final String ATTENDANCE_FILE = "../data/attendance.txt";
    private static final String COMPLAINTS_FILE = "../data/complaints.txt";
    private static final String WARDENS_FILE = "../data/wardens.txt";
    private static final String POLLS_FILE = "../data/polls.txt";
    private static final String VOTES_FILE = "../data/votes.txt";
    private Map<String, Student> students;
    private List<LeaveRequest> leaveRequests;
    private List<Attendance> attendanceRecords;
    private List<Complaint> complaints;
    private Map<String, Warden> wardens;
    private Map<String, Poll> polls;
    private List<Vote> votes;

    public DataManager() {
        students = new HashMap<>();
        leaveRequests = new ArrayList<>();
        attendanceRecords = new ArrayList<>();
        complaints = new ArrayList<>();
        wardens = new HashMap<>();
        polls = new HashMap<>();
        votes = new ArrayList<>();
        loadStudents();
        loadLeaveRequests();
        loadAttendance();
        loadComplaints();
        loadWardens();
        loadPolls();
        loadVotes();
    }

    public void addStudent(Student student) {
        students.put(student.getRollNumber(), student);
        saveStudents();
    }

    public Student getStudent(String rollNumber) {
        return students.get(rollNumber);
    }

    public Map<String, Student> getAllStudents() {
        return students;
    }

    public void addLeaveRequest(LeaveRequest request) {
        leaveRequests.add(request);
        saveLeaveRequests();
    }

    public List<LeaveRequest> getLeaveRequests() {
        return leaveRequests;
    }

    public void updateLeaveRequests(List<LeaveRequest> requests) {
        this.leaveRequests = requests;
        saveLeaveRequests();
    }

    public void addAttendance(Attendance attendance) {
        attendanceRecords.add(attendance);
        saveAttendance();
    }

    public List<Attendance> getAttendanceRecords() {
        return attendanceRecords;
    }

    public void updateAttendance(List<Attendance> records) {
        this.attendanceRecords = records;
        saveAttendance();
    }

    public void addComplaint(Complaint complaint) {
        complaints.add(complaint);
    }

    public List<Complaint> getComplaints() {
        return complaints;
    }

    public void addWarden(Warden warden) {
        wardens.put(warden.getUsername(), warden);
        saveWardens();
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
        polls.put(poll.getPollId(), poll);
        savePolls();
    }

    public Poll getPoll(String pollId) {
        return polls.get(pollId);
    }

    public Map<String, Poll> getAllPolls() {
        return polls;
    }

    public void addVote(Vote vote) {
        votes.add(vote);
        saveVotes();
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

    private void loadStudents() {
        try {
            File file = new File(STUDENTS_FILE);
            if (!file.exists()) {
                file.createNewFile();
            }
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] details = line.split(",");
                if (details.length == 4) {
                    Student student = new Student(details[0], details[1], details[2], details[3]);
                    students.put(student.getRollNumber(), student);
                }
            }
            fileScanner.close();
        } catch (IOException e) {
            System.out.println("An error occurred while loading students.");
        }
    }

    private void saveStudents() {
        try {
            FileWriter writer = new FileWriter(STUDENTS_FILE);
            for (Student student : students.values()) {
                writer.write(student.getName() + "," + student.getRollNumber() + "," + student.getRoomNumber() + "," + student.getPhoneNumber() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while saving students.");
        }
    }

    private void loadLeaveRequests() {
        try {
            File file = new File(LEAVE_REQUESTS_FILE);
            if (!file.exists()) {
                file.createNewFile();
            }
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] details = line.split(",");
                if (details.length == 3) {
                    LeaveRequest request = new LeaveRequest(details[0], details[1], details[2]);
                    leaveRequests.add(request);
                }
            }
            fileScanner.close();
        } catch (IOException e) {
            System.out.println("An error occurred while loading leave requests.");
        }
    }

    private void saveLeaveRequests() {
        try {
            FileWriter writer = new FileWriter(LEAVE_REQUESTS_FILE);
            for (LeaveRequest request : leaveRequests) {
                writer.write(request.getStudentRollNumber() + "," + request.getReason() + "," + request.getStatus() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while saving leave requests.");
        }
    }

    private void loadAttendance() {
        try {
            File file = new File(ATTENDANCE_FILE);
            if (!file.exists()) {
                file.createNewFile();
            }
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] details = line.split(",");
                if (details.length == 4) {
                    Attendance attendance = new Attendance(details[0], details[1], details[2], details[3]);
                    attendanceRecords.add(attendance);
                }
            }
            fileScanner.close();
        } catch (IOException e) {
            System.out.println("An error occurred while loading attendance records.");
        }
    }

    private void saveAttendance() {
        try {
            FileWriter writer = new FileWriter(ATTENDANCE_FILE);
            for (Attendance attendance : attendanceRecords) {
                writer.write(attendance.getStudentRollNumber() + "," + attendance.getDate() + "," + attendance.getInTime() + "," + attendance.getOutTime() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while saving attendance records.");
        }
    }

    private void loadComplaints() {
        try {
            File file = new File(COMPLAINTS_FILE);
            if (!file.exists()) {
                file.createNewFile();
            }
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] details = line.split(",", 2);
                if (details.length == 2) {
                    Complaint complaint = new Complaint(details[0], details[1]);
                    complaints.add(complaint);
                }
            }
            fileScanner.close();
        } catch (IOException e) {
            System.out.println("An error occurred while loading complaints.");
        }
    }

    private void saveComplaints() {
        try {
            FileWriter writer = new FileWriter(COMPLAINTS_FILE);
            for (Complaint complaint : complaints) {
                writer.write(complaint.getStudentRollNumber() + "," + complaint.getDescription() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while saving complaints.");
        }
    }

    private void loadWardens() {
        try {
            File file = new File(WARDENS_FILE);
            if (!file.exists()) {
                file.createNewFile();
                // Add default warden for initial setup
                addWarden(new Warden("Admin Warden", "admin", "password"));
            }
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] details = line.split(",");
                if (details.length == 3) {
                    Warden warden = new Warden(details[0], details[1], details[2]);
                    wardens.put(warden.getUsername(), warden);
                }
            }
            fileScanner.close();
        } catch (IOException e) {
            System.out.println("An error occurred while loading wardens.");
        }
    }

    private void saveWardens() {
        try {
            FileWriter writer = new FileWriter(WARDENS_FILE);
            for (Warden warden : wardens.values()) {
                writer.write(warden.getName() + "," + warden.getUsername() + "," + warden.getPassword() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while saving wardens.");
        }
    }

    private void loadPolls() {
        try {
            File file = new File(POLLS_FILE);
            if (!file.exists()) {
                file.createNewFile();
            }
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] details = line.split("\\|");
                if (details.length >= 4) {
                    String pollId = details[0];
                    String title = details[1];
                    String question = details[2];
                    String optionsStr = details[3];
                    String creator = details[4];
                    String[] optionsArray = optionsStr.split(",");
                    List<String> options = new ArrayList<>();
                    for (String option : optionsArray) {
                        options.add(option.trim());
                    }
                    Poll poll = new Poll(pollId, title, question, options, creator);
                    polls.put(pollId, poll);
                }
            }
            fileScanner.close();
        } catch (IOException e) {
            System.out.println("An error occurred while loading polls.");
        }
    }

    private void savePolls() {
        try {
            FileWriter writer = new FileWriter(POLLS_FILE);
            for (Poll poll : polls.values()) {
                String optionsStr = String.join(",", poll.getOptions());
                writer.write(poll.getPollId() + "|" + poll.getTitle() + "|" + poll.getQuestion() + "|" + optionsStr + "|" + poll.getCreatorWarden() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while saving polls.");
        }
    }

    private void loadVotes() {
        try {
            File file = new File(VOTES_FILE);
            if (!file.exists()) {
                file.createNewFile();
            }
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] details = line.split(",");
                if (details.length == 3) {
                    Vote vote = new Vote(details[0], details[1], details[2]);
                    votes.add(vote);
                }
            }
            fileScanner.close();
        } catch (IOException e) {
            System.out.println("An error occurred while loading votes.");
        }
    }

    private void saveVotes() {
        try {
            FileWriter writer = new FileWriter(VOTES_FILE);
            for (Vote vote : votes) {
                writer.write(vote.getPollId() + "," + vote.getStudentRollNumber() + "," + vote.getSelectedOption() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while saving votes.");
        }
    }
}
