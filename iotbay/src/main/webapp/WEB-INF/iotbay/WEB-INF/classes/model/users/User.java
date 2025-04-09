package model.users;
import java.io.Serializable;

/**
 * Abstract class representing a generic User in the system.
 * 
 * This class is the base for all user types (e.g., Admin, Staff, Customer) and
 * defines shared properties like userID, name, email, password, and addressID.
 * 
 * Key Features:
 * - **Abstract**: Cannot be instantiated directly; only its subclasses (e.g., Admin) can.
 * - **Polymorphism**: Subclass objects (e.g., Admin) retain their type when stored as User,
 *   allowing type recognition using `instanceof` or overridden methods like `getRole()`.
 * - **Shared Properties**: Ensures consistency and reduces duplication across user types.
 * 
 * Example: During login, a subclass object is stored in the session as a User, and its
 * type can be determined later for role-specific behavior.
 */
public abstract class User implements Serializable {
    private final int userID; 
    private String name;
    private String email;
    // Password should be hashed and salted
    private String password;
    private Integer addressID; // Use Integer since it can be null

    public User(int userID, String name, String email, String password) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public int getUserID() {
        return this.userID;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAddressID() {
        return addressID;
    }

    public void setAddressID(Integer addressID) {
        this.addressID = addressID;
    }
}
