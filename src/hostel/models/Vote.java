package hostel.models;

import java.util.Date;

public class Vote {
    private String pollId;
    private String studentRollNumber;
    private String selectedOption;
    private Date voteDate;

    public Vote(String pollId, String studentRollNumber, String selectedOption) {
        this.pollId = pollId;
        this.studentRollNumber = studentRollNumber;
        this.selectedOption = selectedOption;
        this.voteDate = new Date();
    }

    public String getPollId() {
        return pollId;
    }

    public String getStudentRollNumber() {
        return studentRollNumber;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public Date getVoteDate() {
        return voteDate;
    }

    @Override
    public String toString() {
        return "Poll ID: " + pollId + ", Student: " + studentRollNumber +
               ", Option: " + selectedOption + ", Date: " + voteDate.toString();
    }
}
