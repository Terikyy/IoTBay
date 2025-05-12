package model.User;

import model.users.Admin;
import model.users.Customer;
import model.users.Staff;
import model.users.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    User user;
    private final String name = "John Doe";
    private final String email = "john.doe@gmail.com";
    private final String password = "password";

    public UserTest() {
        user = new Customer(name, email, password);
    }

    @Test
    public void testGetName() {
        assertEquals(name, user.getName());
    }

    @Test
    public void testGetEmail() {
        assertEquals(email, user.getEmail());
    }

    @Test
    public void testGetPassword() {
        assertEquals(password, user.getPassword());
    }

    @Test
    public void testSetName() {
        String newName = "Jane Doe";
        user.setName(newName);
        assertEquals(newName, user.getName());
    }

    @Test
    public void testSetEmail() {
        String newEmail = "jane.doe@gmail.com";
        user.setEmail(newEmail);
        assertEquals(newEmail, user.getEmail());
    }

    @Test
    public void testSetPassword() {
        String newPassword = "newpassword";
        user.setPassword(newPassword);
        assertEquals(newPassword, user.getPassword());
    }

    @Test
    public void testSetAndGetAddressID() {
        Integer addressID = 123;
        user.setAddressID(addressID);
        assertEquals(addressID, user.getAddressID());
    }

    @Test
    public void isAdminTest() {
        assertFalse(user.isAdmin());
        Staff staff = new Staff(user);
        assertFalse(staff.isAdmin());
        Admin admin = new Admin(user);
        assertTrue(admin.isAdmin());
    }

    @Test
    public void isStaffTest() {
        assertFalse(user.isStaff());
        Admin admin = new Admin(user);
        assertFalse(admin.isStaff());
        Staff staff = new Staff(user);
        assertTrue(staff.isStaff());
    }
}
