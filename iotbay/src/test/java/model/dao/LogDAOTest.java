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
        // Check if the log already exists
        Log existingLog = logDAO.findById(log.getLogId());
        if (existingLog != null) {
            cleanUpLog();
        }

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
        assertEquals(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(log.getTimestamp().getTime())), new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(result.getTimestamp().getTime())));
        assertEquals(log.getUserId(), result.getUserId());

        cleanUpLog();
    }

    public void cleanUpLog() throws SQLException {
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
