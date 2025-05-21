package model;

import java.sql.Date;

public class Log extends IDObject {
    private final String message;
    private final Date timestamp;
    private final int userId;

    public Log(String message, int userId) {
        super();
        this.message = message;
        this.timestamp = null;
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
}
