package hostel.models;

public class LeaveRequest {
    private String studentRollNumber;
    private String reason;
    private String status;

    public LeaveRequest(String studentRollNumber, String reason) {
        this.studentRollNumber = studentRollNumber;
        this.reason = reason;
        this.status = "Pending";
    }

    public LeaveRequest(String studentRollNumber, String reason, String status) {
        this.studentRollNumber = studentRollNumber;
        this.reason = reason;
        this.status = status;
    }

    public String getStudentRollNumber() {
        return studentRollNumber;
    }

    public String getReason() {
        return reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Roll Number: " + studentRollNumber + ", Reason: " + reason + ", Status: " + status;
    }
}
