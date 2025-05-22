package model;

import model.dao.AbstractDAO;

import java.sql.SQLException;

public abstract class IDObject {
    private int id;

    public IDObject() {//default constructor
        randomizeID();
    }

    protected int getId() {
        return id;
    }

    protected void setId(int id) {
        this.id = id;
    }

    public void randomizeID() {
        this.id = (int) (Math.random() * Integer.MAX_VALUE);
    }

    // This method is used to handle the case where the ID is already in use, which can happen if two objects get the same random ID.
    // The chances of this happen are very low, but this function safeguards against it, if it does happen.
    public static <T extends IDObject> void insert(AbstractDAO<T> dao, T obj) throws SQLException {
        while (true) {
            try {
                dao.insert(obj);
                break;
            } catch (SQLException e) {
                if (!e.getMessage().contains("UNIQUE constraint failed:") || !e.getMessage().contains("ID")) {
                    throw e;
                } else {
                    obj.randomizeID();
                }
            }
        }
    }
}
