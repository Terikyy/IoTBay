package model.users;

public class Staff extends User {

    public Staff(String name, String email, String password) {
        super(name, email, password);
    }

    public Staff(int userId, String name, String email, String password, String phoneNumber, boolean active) {
        super(userId, name, email, password, phoneNumber, active);
    }

    public Staff(User user) {
        this(user.getUserID(), user.getName(), user.getEmail(), user.getPassword(), user.getPhoneNumber(), user.isActive());
    }
}