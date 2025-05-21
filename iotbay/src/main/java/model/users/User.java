package model.users;

import controllers.ConnServlet;
import controllers.LogController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.IDObject;
import model.dao.LogDAO;
import model.dao.StaffDAO;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;

public abstract class User extends IDObject implements Serializable {
    private String name;
    private String email;
    // Password should be hashed and salted
    private String password;
    private boolean active;

    public User(String name, String email, String password) {
        super();
        this.name = name;
        this.email = email;
        this.password = password;
        active = true;
    }

    public User(int userID, String name, String email, String password, boolean active) {
        this(name, email, password);
        this.active = active;
        setId(userID);
    }

    public int getUserID() {
        return getId();
    }

    public boolean isAdmin() {
        return this instanceof Admin;
    }

    public boolean isStaff() {
        return this instanceof Staff;
    }

    public void setStaff(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        HttpSession session = request.getSession();
        StaffDAO staffDAO = (StaffDAO) session.getAttribute("staffDAO");
        if (staffDAO == null) {
            ConnServlet.updateDAOsPOST(request, response);
            return;
        }
        LogDAO logDAO = (LogDAO) session.getAttribute("logDAO");
        if (logDAO == null) {
            ConnServlet.updateDAOsPOST(request, response);
            return;
        }
        try {
            staffDAO.insert(getUserID());
            LogController.createLog(request, response, "User's role was set to staff", getUserID());
        } catch (SQLException ignored) {
        }
        new Staff(this);
    }

    public void setCustomer(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        HttpSession session = request.getSession();
        StaffDAO staffDAO = (StaffDAO) session.getAttribute("staffDAO");
        if (staffDAO == null) {
            ConnServlet.updateDAOsPOST(request, response);
            return;
        }
        try {
            staffDAO.delete(getUserID());
            LogController.createLog(request, response, "User's role was set to customer", getUserID());
        } catch (SQLException ignored) {
        }
        new Customer(this);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
