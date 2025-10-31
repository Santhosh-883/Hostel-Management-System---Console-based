package hostel.models;

public class LeaveRequest {
    private String studentRollNumber;
    private String reason;
    private String date;
    private String hour;
    private String status;

    public LeaveRequest(String studentRollNumber, String reason, String date, String hour) {
        this.studentRollNumber = studentRollNumber;
        this.reason = reason;
        this.date = date;
        this.hour = hour;
        this.status = "Pending";
    }

    public LeaveRequest(String studentRollNumber, String reason, String date, String hour, String status) {
        this.studentRollNumber = studentRollNumber;
        this.reason = reason;
        this.date = date;
        this.hour = hour;
        this.status = status;
    }

    public String getStudentRollNumber() {
        return studentRollNumber;
    }

    public String getReason() {
        return reason;
    }

    public String getDate() {
        return date;
    }

    public String getHour() {
        return hour;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Roll Number: " + studentRollNumber + ", Reason: " + reason + ", Date: " + date + ", Hour: " + hour + ", Status: " + status;
    }
}
