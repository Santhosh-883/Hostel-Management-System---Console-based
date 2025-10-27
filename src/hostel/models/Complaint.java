package hostel.models;

public class Complaint {
    private String studentRollNumber;
    private String description;

    public Complaint(String studentRollNumber, String description) {
        this.studentRollNumber = studentRollNumber;
        this.description = description;
    }

    public String getStudentRollNumber() {
        return studentRollNumber;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Roll Number: " + studentRollNumber + ", Complaint: " + description;
    }
}
