package model;

import java.sql.Date;

public class Log extends IDObject {
    private String message;
    private Date timestamp;
    private int userId;

    public Log() {//default constructor
        super();
    }

    public Log(String message, int userId) {
        super();
        this.message = message;
        this.timestamp = new Date(System.currentTimeMillis());
        this.userId = userId;
    }

    public Log(int logId, String message, Date timestamp, int userId) {
        this.message = message;
        this.timestamp = timestamp;
        this.userId = userId;
        setId(logId);
    }

    public int getLogId() {
        return getId();
    }

    public int getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    // There are no set methods for this class, as it is immutable after creation.
    // Logs should not be tampered with.
}
