package model;

import model.dao.AbstractDAO;

import java.sql.SQLException;

public abstract class IDObject {
    private int id;

    public IDObject() {
        randomizeID();
    }

    protected int getId() {
        return id;
    }

    public void randomizeID() {
        this.id = (int) (Math.random() * Integer.MAX_VALUE);
    }

    public static <T extends IDObject> void insert(AbstractDAO<T> dao, T obj) throws SQLException { // This is dangerous, but sadly Java generics inheritance doesnt allow us to use AbstractDAO<IDObject> here
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
