package model.users;

public class Customer extends User {

    public Customer(String name, String email, String password) {
        super(name, email, password);
    }

    public Customer(User user) {
        super(user.getName(), user.getEmail(), user.getPassword());
    }

}
