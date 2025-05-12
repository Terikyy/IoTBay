package model.User;

import model.users.Staff;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StaffTest {
    private Staff staff;
    private final String name = "Staff";
    private final String email = "staff@mail.com";
    private final String password = "staff123";

    public StaffTest() {
        this.staff = new Staff(name, email, password);
    }

    @Test
    public void testInitialization() {
        assertEquals(name, staff.getName());
        assertEquals(email, staff.getEmail());
        assertEquals(password, staff.getPassword());
        assertTrue(staff.isStaff());
        int userId = staff.getUserID();
        staff = new Staff(staff);
        assertEquals(name, staff.getName());
        assertEquals(email, staff.getEmail());
        assertEquals(password, staff.getPassword());
        assertTrue(staff.isStaff());
        assertEquals(userId, staff.getUserID());
    }
}
