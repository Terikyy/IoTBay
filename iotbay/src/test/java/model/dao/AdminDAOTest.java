package model.dao;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class AdminDAOTest {
    AdminDAO adminDAO;

    public AdminDAOTest() throws SQLException, ClassNotFoundException {
        DBConnector db = new DBConnector();
        try {
            adminDAO = new AdminDAO(db.openConnection());
        } catch (Exception e) {
            fail("Failed to initialize AdminDAO: " + e.getMessage());
        }
    }

    @Test
    public void testInsert() {
        try {
            adminDAO.insert(Integer.MAX_VALUE);
            fail("Insert is not allowed");
        } catch (Exception e) {
            // Expected exception, do nothing
        }
    }

    @Test
    public void testDelete() {
        try {
            adminDAO.delete(Integer.MAX_VALUE);
            fail("Delete is not allowed");
        } catch (Exception e) {
            // Expected exception, do nothing
        }
    }

    @Test
    public void testIsAdmin() throws SQLException {
        boolean result = adminDAO.isAdmin(0);
        assertTrue(result);
    }

    @Test
    public void testIsNotAdmin() throws SQLException {
        boolean result = adminDAO.isAdmin(Integer.MAX_VALUE);
        assertFalse(result);
    }
}
