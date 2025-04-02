package models;

import java.io.Serializable;

// to be removed

public /*abstract*/ class User implements Serializable {

    String email;
    String name;
    // Password should be hashed and salted (and peppered :))
    String password;

    // Address information (optional)
    // In a real-world application, you might want to use a more complex structure for addresses
    String adressLine = "";
    String additionalAdressInfo = "";
    Integer postalCode;
    String StateCode = "";

    // As adress information is optional, we can have a constructor without it
    public User(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAdressLine() {
        return this.adressLine;
    }

    public void setAdressLine(String adressLine) {
        this.adressLine = adressLine;
    }

    public String getAdditionalAdressInfo() {
        return this.additionalAdressInfo;
    }

    public void setAdditionalAdressInfo(String additionalAdressInfo) {
        this.additionalAdressInfo = additionalAdressInfo;
    }

    public Integer getPostalCode() {
        return this.postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }

    public String getStateCode() {
        return this.StateCode;
    }

    public void setStateCode(String StateCode) {
        this.StateCode = StateCode;
    }
}
