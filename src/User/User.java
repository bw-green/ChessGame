package User;

public class User {
    private final String id;
    private final String pw;
    public User(String id, String pw) {
        this.id = id;
        this.pw = pw;
    }

    public String getId() {
        return id;
    }

    public boolean matchPw(String pw) {
        return this.pw.equals(pw);
    }
}
