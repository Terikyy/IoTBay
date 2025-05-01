package model.users;

public class Admin extends User {

    public Admin(Integer userID, String name, String email, String password) {
        super(userID, name, email, password);
    }

    public Admin(User user) {
        super(user.getUserID(), user.getName(), user.getEmail(), user.getPassword());
    }

}