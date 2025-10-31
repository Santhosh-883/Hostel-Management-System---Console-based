package hostel.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Attendance {
    private String studentRollNumber;
    private String date;
    private String inTime;
    private String outTime;

    public Attendance(String studentRollNumber) {
        this.studentRollNumber = studentRollNumber;
        this.date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        this.inTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
        this.outTime = "Not Marked";
    }

    public Attendance(String studentRollNumber, String date, String inTime, String outTime) {
        this.studentRollNumber = studentRollNumber;
        this.date = date;
        this.inTime = inTime;
        this.outTime = outTime;
    }

    public String getStudentRollNumber() {
        return studentRollNumber;
    }

    public String getDate() {
        return date;
    }

    public String getInTime() {
        return inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public void markOutTime() {
        this.outTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    @Override
    public String toString() {
        return "Roll Number: " + studentRollNumber + ", Date: " + date + ", In-Time: " + inTime + ", Out-Time: " + outTime;
    }
}
