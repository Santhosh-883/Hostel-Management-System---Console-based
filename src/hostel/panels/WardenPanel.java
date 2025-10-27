package hostel.panels;

import hostel.data.DataManager;
import hostel.models.Attendance;
import hostel.models.Complaint;
import hostel.models.LeaveRequest;
import hostel.models.Poll;
import hostel.models.Student;
import hostel.models.Warden;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

public class WardenPanel {
    private DataManager dataManager;
    private Scanner scanner;
    private Warden currentWarden;
    public WardenPanel(DataManager dataManager) {
        this.dataManager = dataManager;
        this.scanner = new Scanner(System.in);
    }

    public void showMenu() {
        if (currentWarden == null) {
            login();
        }
        boolean running = true;
        while (running) {
            System.out.println("\n--- Warden Panel ---");
            System.out.println("Welcome, " + currentWarden.getName() + "!");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. View Students by Room");
            System.out.println("4. Manage Leave Requests");
            System.out.println("5. View Attendance Records");
            System.out.println("6. View Complaints");
            System.out.println("7. Daily Attendance Report");
            System.out.println("8. Create Poll");
            System.out.println("9. View Poll Results");
            System.out.println("10. Logout");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    viewAllStudents();
                    break;
                case 3:
                    viewStudentsByRoom();
                    break;
                case 4:
                    manageLeaveRequests();
                    break;
                case 5:
                    viewAttendanceRecords();
                    break;
                case 6:
                    viewComplaints();
                    break;
                case 7:
                    viewDailyAttendanceReport();
                    break;
                case 8:
                    createPoll();
                    break;
                case 9:
                    viewPollResults();
                    break;
                case 10:
                    logout();
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void login() {
        System.out.println("\n--- Warden Login ---");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (dataManager.authenticateWarden(username, password)) {
            currentWarden = dataManager.getWarden(username);
            System.out.println("Login successful! Welcome, " + currentWarden.getName());
        } else {
            System.out.println("Invalid username or password. Please try again.");
            login(); // Retry login
        }
    }

    private void logout() {
        currentWarden = null;
        System.out.println("Logged out successfully.");
    }

    private void addStudent() {
        System.out.println("\n--- Add New Student ---");
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        System.out.print("Enter roll number: ");
        String rollNumber = scanner.nextLine();
        System.out.print("Enter room number: ");
        String roomNumber = scanner.nextLine();
        System.out.print("Enter phone number: ");
        String phoneNumber = scanner.nextLine();

        Student student = new Student(name, rollNumber, roomNumber, phoneNumber);
        dataManager.addStudent(student);

        System.out.println("Student added successfully!");
    }

    private void viewAllStudents() {
        System.out.println("\n--- All Students ---");
        if (dataManager.getAllStudents().isEmpty()) {
            System.out.println("No students found.");
        } else {
            for (Student student : dataManager.getAllStudents().values()) {
                System.out.println(student);
            }
        }
    }

    private void viewStudentsByRoom() {
        System.out.println("\n--- Students by Room ---");
        Map<String, Student> allStudents = dataManager.getAllStudents();
        Map<String, List<Student>> studentsByRoom = new HashMap<>();

        // Group students by room
        for (Student student : allStudents.values()) {
            String room = student.getRoomNumber();
            studentsByRoom.computeIfAbsent(room, k -> new ArrayList<>()).add(student);
        }

        if (studentsByRoom.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        // Display students grouped by room
        for (Map.Entry<String, List<Student>> entry : studentsByRoom.entrySet()) {
            String room = entry.getKey();
            List<Student> studentsInRoom = entry.getValue();
            System.out.println("\nRoom " + room + " (" + studentsInRoom.size() + " students):");
            for (Student student : studentsInRoom) {
                System.out.println("  - " + student.getName() + " (" + student.getRollNumber() + ")");
            }
        }

        // Note unallocated rooms (for now, since we don't have predefined rooms, we can mention it)
        System.out.println("\nNote: Rooms without students are considered unallocated.");
    }

    private void manageLeaveRequests() {
        System.out.println("\n--- Manage Leave Requests ---");
        List<LeaveRequest> requests = dataManager.getLeaveRequests();

        if (requests.isEmpty()) {
            System.out.println("No leave requests found.");
            return;
        }

        for (int i = 0; i < requests.size(); i++) {
            System.out.println((i + 1) + ". " + requests.get(i));
        }

        System.out.print("Enter the number of the request to manage (0 to cancel): ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice > 0 && choice <= requests.size()) {
            LeaveRequest request = requests.get(choice - 1);

            System.out.print("Approve (a) or Reject (r): ");
            String action = scanner.nextLine();

            if (action.equalsIgnoreCase("a")) {
                request.setStatus("Approved");
            } else if (action.equalsIgnoreCase("r")) {
                request.setStatus("Rejected");
            }

            dataManager.updateLeaveRequests(requests);
            System.out.println("Leave request updated.");
        }
    }

    private void viewAttendanceRecords() {
        System.out.println("\n--- Attendance Records ---");
        List<Attendance> records = dataManager.getAttendanceRecords();

        if (records.isEmpty()) {
            System.out.println("No attendance records found.");
        } else {
            for (Attendance record : records) {
                System.out.println(record);
            }
        }
    }

    private void viewComplaints() {
        System.out.println("\n--- Student Complaints ---");
        List<Complaint> complaints = dataManager.getComplaints();

        if (complaints.isEmpty()) {
            System.out.println("No complaints found.");
        } else {
            for (Complaint complaint : complaints) {
                System.out.println(complaint);
            }
        }
    }

    private void viewDailyAttendanceReport() {
        System.out.println("\n--- Daily Attendance Report ---");
        Map<String, Student> allStudents = dataManager.getAllStudents();
        List<Attendance> todayAttendance = dataManager.getAttendanceRecords();

        String today = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());

        System.out.println("Students who marked attendance:");
        boolean hasAttendance = false;

        for (Attendance attendance : todayAttendance) {
            if (attendance.getDate().equals(today)) {
                Student student = allStudents.get(attendance.getStudentRollNumber());
                if (student != null) {
                    System.out.println("- " + student.getName() + " (" + student.getRollNumber() + ") - In: " + attendance.getInTime() + ", Out: " + attendance.getOutTime());
                    hasAttendance = true;
                }
            }
        }

        if (!hasAttendance) {
            System.out.println("No students marked attendance today.");
        }

        System.out.println("\nStudents who did NOT mark attendance:");
        boolean hasNoAttendance = false;

        for (Student student : allStudents.values()) {
            boolean found = false;
            for (Attendance attendance : todayAttendance) {
                if (attendance.getStudentRollNumber().equals(student.getRollNumber()) && attendance.getDate().equals(today)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("- " + student.getName() + " (" + student.getRollNumber() + ")");
                hasNoAttendance = true;
            }
        }

        if (!hasNoAttendance) {
            System.out.println("All students marked attendance today.");
        }
    }

    private void createPoll() {
        System.out.println("\n--- Create Poll ---");
        System.out.print("Enter poll title: ");
        String title = scanner.nextLine();
        System.out.print("Enter poll question: ");
        String question = scanner.nextLine();

        List<String> options = new ArrayList<>();
        System.out.print("Enter number of options (2-4): ");
        int numOptions = scanner.nextInt();
        scanner.nextLine();

        for (int i = 1; i <= numOptions; i++) {
            System.out.print("Enter option " + i + ": ");
            String option = scanner.nextLine();
            options.add(option);
        }

        String pollId = UUID.randomUUID().toString();
        Poll poll = new Poll(pollId, title, question, options, currentWarden.getName());
        dataManager.addPoll(poll);

        System.out.println("Poll created successfully! Poll ID: " + pollId);
    }

    private void viewPollResults() {
        System.out.println("\n--- Poll Results ---");
        Map<String, Poll> allPolls = dataManager.getAllPolls();

        if (allPolls.isEmpty()) {
            System.out.println("No polls found.");
            return;
        }

        for (Poll poll : allPolls.values()) {
            System.out.println("\nPoll: " + poll.getTitle() + " (ID: " + poll.getPollId() + ")");
            System.out.println("Question: " + poll.getQuestion());
            System.out.println("Created by: " + poll.getCreatorWarden());

            Map<String, Integer> results = dataManager.getPollResults(poll.getPollId());
            int totalVotes = 0;
            for (int count : results.values()) {
                totalVotes += count;
            }

            System.out.println("Results:");
            for (Map.Entry<String, Integer> entry : results.entrySet()) {
                String option = entry.getKey();
                int votes = entry.getValue();
                double percentage = totalVotes > 0 ? (votes * 100.0) / totalVotes : 0;
                System.out.printf("  %s: %d votes (%.2f%%)\n", option, votes, percentage);
            }
            System.out.println("Total votes: " + totalVotes);
        }
    }
}
