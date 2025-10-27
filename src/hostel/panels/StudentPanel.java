package hostel.panels;

import hostel.data.DataManager;
import hostel.models.Attendance;
import hostel.models.Complaint;
import hostel.models.LeaveRequest;
import hostel.models.Poll;
import hostel.models.Student;
import hostel.models.Vote;

import java.util.List;

import java.util.Map;
import java.util.Scanner;

public class StudentPanel {
    private DataManager dataManager;
    private Scanner scanner;
    private Student currentStudent;

    public StudentPanel(DataManager dataManager) {
        this.dataManager = dataManager;
        this.scanner = new Scanner(System.in);
    }

    public void login() {
        System.out.println("\n--- Student Login ---");
        System.out.print("Enter your roll number to login: ");
        String rollNumber = scanner.nextLine();
        currentStudent = dataManager.getStudent(rollNumber);

        if (currentStudent != null) {
            System.out.println("Login successful! Welcome, " + currentStudent.getName());
            showMenu();
        } else {
            System.out.println("Student not found. Please try again.");
        }
    }

    public void showMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Student Panel ---");
            System.out.println("1. View Personal Details");
            System.out.println("2. Request Leave");
            System.out.println("3. Check Leave Status");
            System.out.println("4. Mark Attendance");
            System.out.println("5. Raise Complaint");
            System.out.println("6. Participate in Polls");
            System.out.println("7. Logout");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewPersonalDetails();
                    break;
                case 2:
                    requestLeave();
                    break;
                case 3:
                    checkLeaveStatus();
                    break;
                case 4:
                    markAttendance();
                    break;
                case 5:
                    raiseComplaint();
                    break;
                case 6:
                    participateInPolls();
                    break;
                case 7:
                    currentStudent = null;
                    running = false;
                    System.out.println("Logged out successfully.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void viewPersonalDetails() {
        System.out.println("\n--- Your Personal Details ---");
        System.out.println(currentStudent);
    }

    private void requestLeave() {
        System.out.println("\n--- Request Leave ---");
        System.out.print("Enter reason for leave: ");
        String reason = scanner.nextLine();

        LeaveRequest request = new LeaveRequest(currentStudent.getRollNumber(), reason);
        dataManager.addLeaveRequest(request);

        System.out.println("Leave request submitted successfully!");
    }

    private void checkLeaveStatus() {
        System.out.println("\n--- Your Leave Requests ---");
        List<LeaveRequest> requests = dataManager.getLeaveRequests();
        boolean found = false;

        for (LeaveRequest request : requests) {
            if (request.getStudentRollNumber().equals(currentStudent.getRollNumber())) {
                System.out.println("Reason: " + request.getReason() + " | Status: " + request.getStatus());
                found = true;
            }
        }

        if (!found) {
            System.out.println("No leave requests found for you.");
        }
    }

    private void markAttendance() {
        System.out.println("\n--- Mark Attendance ---");
        System.out.println("1. Mark In-Time");
        System.out.println("2. Mark Out-Time");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1) {
            Attendance attendance = new Attendance(currentStudent.getRollNumber());
            dataManager.addAttendance(attendance);
            System.out.println("In-Time marked successfully!");
        } else if (choice == 2) {
            List<Attendance> records = dataManager.getAttendanceRecords();
            for (int i = records.size() - 1; i >= 0; i--) {
                Attendance record = records.get(i);
                if (record.getStudentRollNumber().equals(currentStudent.getRollNumber()) && record.getOutTime().equals("Not Marked")) {
                    record.markOutTime();
                    dataManager.updateAttendance(records);
                    System.out.println("Out-Time marked successfully!");
                    return;
                }
            }
            System.out.println("You have not marked your In-Time today.");
        }
    }

    private void raiseComplaint() {
        System.out.println("\n--- Raise a Complaint ---");
        System.out.print("Enter your complaint: ");
        String description = scanner.nextLine();

        Complaint complaint = new Complaint(currentStudent.getRollNumber(), description);
        dataManager.addComplaint(complaint);

        System.out.println("Complaint raised successfully!");
    }

    private void participateInPolls() {
        System.out.println("\n--- Participate in Polls ---");
        Map<String, Poll> allPolls = dataManager.getAllPolls();

        if (allPolls.isEmpty()) {
            System.out.println("No polls available.");
            return;
        }

        System.out.println("Available polls:");
        int index = 1;
        for (Poll poll : allPolls.values()) {
            System.out.println(index + ". " + poll.getTitle() + " (ID: " + poll.getPollId() + ")");
            System.out.println("   Question: " + poll.getQuestion());
            System.out.println("   Created by: " + poll.getCreatorWarden());
            index++;
        }

        System.out.print("Enter poll number to vote (0 to cancel): ");
        int pollChoice = scanner.nextInt();
        scanner.nextLine();

        if (pollChoice == 0) {
            return;
        }

        if (pollChoice < 1 || pollChoice > allPolls.size()) {
            System.out.println("Invalid poll choice.");
            return;
        }

        Poll selectedPoll = (Poll) allPolls.values().toArray()[pollChoice - 1];
        String pollId = selectedPoll.getPollId();

        // Check if student has already voted
        if (dataManager.hasStudentVoted(pollId, currentStudent.getRollNumber())) {
            System.out.println("You have already voted in this poll.");
            return;
        }

        // Display options and get vote
        System.out.println("\nPoll: " + selectedPoll.getTitle());
        System.out.println("Question: " + selectedPoll.getQuestion());
        System.out.println("Options:");
        List<String> options = selectedPoll.getOptions();
        for (int i = 0; i < options.size(); i++) {
            System.out.println((i + 1) + ". " + options.get(i));
        }

        System.out.print("Enter your choice (1-" + options.size() + "): ");
        int voteChoice = scanner.nextInt();
        scanner.nextLine();

        if (voteChoice < 1 || voteChoice > options.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        String selectedOption = options.get(voteChoice - 1);
        Vote vote = new Vote(pollId, currentStudent.getRollNumber(), selectedOption);
        dataManager.addVote(vote);

        System.out.println("Your vote has been recorded successfully!");
    }
}
