package model;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class LogTest {
    private final Log log;
    private final String message = "Test log message";
    private final int id = 1234;
    private final Date timestamp = new Date();
    private final int userId = 5678;

    public LogTest() {
        log = new Log(id, message, timestamp, userId);
    }

    @Test
    public void testGetLogId() {
        assertEquals(id, log.getLogId());
    }

    @Test
    public void testGetMessage() {
        assertEquals(message, log.getMessage());
    }

    @Test
    public void testGetTimestamp() {
        assertEquals(timestamp, log.getTimestamp());
    }

    @Test
    public void testConstructorWithMessage() {
        Log logWithMessage = new Log(message, userId);
        assertEquals(message, logWithMessage.getMessage());
        assertEquals(userId, logWithMessage.getUserId());
        assertTrue(logWithMessage.getLogId() >= 0 && logWithMessage.getLogId() < Integer.MAX_VALUE);
        assertNull(logWithMessage.getTimestamp());
    }
}
