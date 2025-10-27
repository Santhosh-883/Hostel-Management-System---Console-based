package hostel.models;

public class Warden {
    private String username;
    private String password;
    private String name;

    public Warden(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Username: " + username;
    }
}
