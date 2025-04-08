package model.users;
import java.io.Serializable;

public abstract class User implements Serializable{
    private final Integer userID;
    private String name;
    private String email;
    // Password should be hashed and salted
    private String password;
    private Integer addressID;

    public User(Integer userID, String email, String password) {
        this.userID = userID;
        this.name = "";
        this.email = email;
        this.password = password;
    }

    public Integer getUserID() {
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

    public int getAddressID() {
        return addressID;
    }

    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }
}
