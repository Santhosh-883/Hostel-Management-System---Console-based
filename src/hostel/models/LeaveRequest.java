package hostel.models;

public class LeaveRequest {
    private String studentRollNumber;
    private String reason;
    private String fromDate;
    private String toDate;
    private String hour;
    private String status;

    public LeaveRequest(String studentRollNumber, String reason, String fromDate, String toDate, String hour) {
        this.studentRollNumber = studentRollNumber;
        this.reason = reason;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.hour = hour;
        this.status = "Pending";
    }

    public LeaveRequest(String studentRollNumber, String reason, String fromDate, String toDate, String hour, String status) {
        this.studentRollNumber = studentRollNumber;
        this.reason = reason;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.hour = hour;
        this.status = status;
    }

    public String getStudentRollNumber() {
        return studentRollNumber;
    }

    public String getReason() {
        return reason;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getToDate() {
        return toDate;
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
        if (toDate == null || fromDate.equals(toDate)) {
            return "Roll Number: " + studentRollNumber + ", Reason: " + reason + ", Date: " + fromDate + ", Hour: " + hour + ", Status: " + status;
        } else {
            return "Roll Number: " + studentRollNumber + ", Reason: " + reason + ", From: " + fromDate + ", To: " + toDate + ", Hour: " + hour + ", Status: " + status;
        }
    }
}
