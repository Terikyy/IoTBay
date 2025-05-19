package model;

import java.util.Date;

public class Log extends IDObject {
    private final String message;
    private final Date timestamp;

    public Log(String message) {
        this(message, null);
    }

    public Log(String message, Date timestamp) {
        super();
        this.message = message;
        this.timestamp = timestamp;
    }

    public Log(int logId, String message, Date timestamp) {
        this(message, timestamp);
        setId(logId);
    }

    public int getLogId() {
        return getId();
    }

    public String getMessage() {
        return message;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
