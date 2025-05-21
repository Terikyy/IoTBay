package model.dao;

import model.Log;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class LogDAOTest {
    LogDAO logDAO;
    Log log = new Log(Integer.MAX_VALUE, "Test Log", new Date(System.currentTimeMillis()), Integer.MAX_VALUE);

    public LogDAOTest() throws SQLException, ClassNotFoundException {
        DBConnector db = new DBConnector();
        try {
            logDAO = new LogDAO(db.openConnection());
        } catch (Exception e) {
            fail("Failed to initialize LogDAO: " + e.getMessage());
        }
    }

    @Test
    public void testInsert() throws SQLException {
        try {
            logDAO.insert(log);
        } catch (Exception e) {
            fail("Insert failed: " + e.getMessage());
        }
        Log result = logDAO.findById(log.getLogId());
        if (result == null) {
            fail("Log not found after insert");
        }
        assertEquals(log.getLogId(), result.getLogId());
        assertEquals(log.getMessage(), result.getMessage());
        assertEquals(log.getTimestamp(), result.getTimestamp());
        assertEquals(log.getUserId(), result.getUserId());

        // Clean up the inserted log
        String query = "DELETE FROM Log WHERE LogID = ?";
        try (PreparedStatement ps = logDAO.conn.prepareStatement(query)) {
            ps.setInt(1, log.getLogId());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected <= 0) {
                fail("Log not deleted after insert");
            }
        }
    }

    @Test
    public void testDelete() throws SQLException {
        try {
            logDAO.deleteById(log.getLogId());
            fail("Logs should not be deleted");
        } catch (Exception e) {
            // Expected exception, do nothing
        }
    }

    @Test
    public void testUpdate() throws SQLException {
        try {
            logDAO.update(log);
            fail("Logs should not be updated");
        } catch (Exception e) {
            // Expected exception, do nothing
        }
    }
}
