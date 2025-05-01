package model.users;

public class Staff extends User {

    public Staff(Integer userID, String name, String email, String password) {
        super(userID, name, email, password);
    }

    public Staff(User user) {
        super(user.getUserID(), user.getName(), user.getEmail(), user.getPassword());
    }
}