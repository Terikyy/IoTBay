package model.User;

import model.users.Admin;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AdminTest {
    private Admin admin;
    private final String name = "Admin";
    private final String email = "admin@mail.com";
    private final String password = "admin123";

    public AdminTest() {
        this.admin = new Admin(name, email, password);
    }

    @Test
    public void testInitialization() {
        assertEquals(name, admin.getName());
        assertEquals(email, admin.getEmail());
        assertEquals(password, admin.getPassword());
        assertTrue(admin.isAdmin());
        int userId = admin.getUserID();
        admin = new Admin(admin);
        assertEquals(name, admin.getName());
        assertEquals(email, admin.getEmail());
        assertEquals(password, admin.getPassword());
        assertTrue(admin.isAdmin());
        assertEquals(userId, admin.getUserID());
    }
}
