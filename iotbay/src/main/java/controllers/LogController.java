package controllers;

import jakarta.servlet.http.HttpSession;
import model.dao.LogDAO;

public class LogController {
    public void createLog() {

    }

    // Example method to retrieve logs
    public void getLogs(HttpSession session) {
        LogDAO logDAO = (LogDAO) session.getAttribute("logDAO");
        // send redirect to login page using session

    }

    // Example method to delete a log entry
    public void deleteLog(int logId) {
        // Implementation for deleting a log entry
    }
}
