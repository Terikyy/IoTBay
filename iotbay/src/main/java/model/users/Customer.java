package model.users;

public class Customer extends User {

    public Customer(String name, String email, String password) {
        super(name, email, password);
    }

    public Customer(int userId, String name, String email, String password) {
        super(userId, name, email, password);
    }

    public Customer(User user) {
        super(user.getUserID(), user.getName(), user.getEmail(), user.getPassword());
    }

}
