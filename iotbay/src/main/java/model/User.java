package model;
import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable{

  String email;
  String name;
  // Password should be hashed and salted
  String password;
  String gender;

  // Address information (optional)
  // In a real-world application, you might want to use a more complex structure for addresses
  String adressLine="";
  String additionalAdressInfo="";
  Integer postalCode;
  String StateCode="";

  public User() {
  }

  // As adress information is optional, we can have a constructor without it
  public User(String email, String name, String password, String gender) {
    this.email = email;
    this.name = name;
    this.password = password;
    this.gender = gender;
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

  public String getGender() {
    return this.gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
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
  
  @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(email, user.email) && Objects.equals(name, user.name) && Objects.equals(password, user.password) && Objects.equals(gender, user.gender) && Objects.equals(adressLine, user.adressLine) && Objects.equals(additionalAdressInfo, user.additionalAdressInfo) && Objects.equals(postalCode, user.postalCode) && Objects.equals(StateCode, user.StateCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(email, name, password, gender);
  }

  @Override
  public String toString() {
    return "{" +
      " email='" + getEmail() + "'" +
      ", name='" + getName() + "'" +
      ", password='" + getPassword() + "'" +
      ", gender='" + getGender() + "'" +
      "}";
  }
  
}
