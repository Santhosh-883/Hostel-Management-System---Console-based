package hostel.models;

public class Student {
    private String name;
    private String rollNumber;
    private String roomNumber;
    private String phoneNumber;

    public Student(String name, String rollNumber, String roomNumber, String phoneNumber) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.roomNumber = roomNumber;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Roll Number: " + rollNumber + ", Room Number: " + roomNumber + ", Phone: " + phoneNumber;
    }
}
