package hostel.models;

import java.util.Date;
import java.util.List;

public class Poll {
    private String pollId;
    private String title;
    private String question;
    private List<String> options;
    private String creatorWarden;
    private Date createdDate;

    public Poll(String pollId, String title, String question, List<String> options, String creatorWarden) {
        this.pollId = pollId;
        this.title = title;
        this.question = question;
        this.options = options;
        this.creatorWarden = creatorWarden;
        this.createdDate = new Date();
    }

    public String getPollId() {
        return pollId;
    }

    public String getTitle() {
        return title;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getCreatorWarden() {
        return creatorWarden;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    @Override
    public String toString() {
        return "Poll ID: " + pollId + ", Title: " + title + ", Question: " + question +
               ", Created by: " + creatorWarden + ", Date: " + createdDate.toString();
    }
}
