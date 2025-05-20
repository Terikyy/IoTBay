package model.users;

public class Staff extends User {

    public Staff(String name, String email, String password) {
        super(name, email, password);
    }

    public Staff(int userId, String name, String email, String password, boolean active, Integer addressID) {
        super(userId, name, email, password, active, addressID);
    }

    public Staff(User user) {
        this(user.getUserID(), user.getName(), user.getEmail(), user.getPassword(), user.isActive(), user.getAddressID());
    }
}