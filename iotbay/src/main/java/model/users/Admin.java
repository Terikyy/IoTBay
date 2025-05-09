package model.users;

public class Admin extends User {

    public Admin(String name, String email, String password) {
        super(name, email, password);
    }

    public Admin(int userId, String name, String email, String password) {
        super(userId, name, email, password);
    }

    public Admin(User user) {
        super(user.getUserID(), user.getName(), user.getEmail(), user.getPassword());
    }

}