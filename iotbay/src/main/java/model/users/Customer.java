package model.users;

public class Customer extends User {

    public Customer(Integer userID, String name, String email, String password) {
        super(userID, name, email, password);
    }

    public Customer(User user) {
        super(user.getUserID(), user.getName(), user.getEmail(), user.getPassword());
    }

}
