package controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Log;
import model.dao.LogDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class LogController {
    public static void createLog(HttpServletRequest request, HttpServletResponse response, String message, int userId) throws IOException, SQLException {
        HttpSession session = request.getSession();
        LogDAO logDAO = (LogDAO) session.getAttribute("logDAO");
        if (logDAO == null) {
            ConnServlet.updateDAOsPOST(request, response);
            return;
        }

        Log log = new Log(message, userId);

        logDAO.insert(log);
    }

    // Example method to retrieve logs
    public static List<Log> getLogs(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        HttpSession session = request.getSession();
        LogDAO logDAO = (LogDAO) session.getAttribute("logDAO");
        if (logDAO == null) {
            ConnServlet.updateDAOsGET(request, response);
            return null;
        }

        return logDAO.getAll();
    }

    public static List<Log> queryLogs(String query, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        HttpSession session = request.getSession();
        LogDAO logDAO = (LogDAO) session.getAttribute("logDAO");
        if (logDAO == null) {
            ConnServlet.updateDAOsGET(request, response);
            return null;
        }

        return logDAO.query(query);
    }

    // Example method to delete a log entry
    public static void deleteLog(int logId) {
        throw new UnsupportedOperationException("Logs should not be tampered with!!");
    }
}
