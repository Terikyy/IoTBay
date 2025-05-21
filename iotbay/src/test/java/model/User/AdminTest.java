package model.User;

import model.users.Admin;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AdminTest {
    private Admin admin;
    private final int userId = 0;
    private final String name = "Admin";
    private final String email = "admin@mail.com";
    private final String password = "admin123";
    private final String phoneNumber = "+610412345678";
    private final boolean active = true;

    public AdminTest() {
        this.admin = new Admin(userId, name, email, password, phoneNumber, active);
    }

    @Test
    public void testInitialization1() {
        assertEquals(userId, admin.getUserID());
        assertEquals(name, admin.getName());
        assertEquals(email, admin.getEmail());
        assertEquals(password, admin.getPassword());
        assertEquals(phoneNumber, admin.getPhoneNumber());
        assertTrue(admin.isActive());
        assertTrue(admin.isAdmin());
    }

    @Test
    public void testInitialization2() {
        Admin admin = new Admin(name, email, password);
        assertTrue(admin.getUserID() >= 0 && admin.getUserID() < Integer.MAX_VALUE);
        assertEquals(name, admin.getName());
        assertEquals(email, admin.getEmail());
        assertEquals(password, admin.getPassword());
        assertTrue(admin.isActive());
        assertTrue(admin.isAdmin());
    }
}
