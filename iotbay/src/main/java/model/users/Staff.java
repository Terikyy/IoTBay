package model.users;

public class Staff extends User {

    public Staff(String name, String email, String password) {
        super(name, email, password);
    }

    public Staff(int userId, String name, String email, String password) {
        super(userId, name, email, password);
    }

    public Staff(User user) {
        super(user.getUserID(), user.getName(), user.getEmail(), user.getPassword());
    }
}