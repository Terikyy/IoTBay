package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IDObjectTest {
    private static class NonAbstractIDObject extends IDObject {
        public NonAbstractIDObject(int id) {
            super();
            setId(id);
        }

        public NonAbstractIDObject() {
            super();
        }
        // This class is used to create an instance of IDObject for testing purposes
    }

    private final IDObject idObject;
    private final int id = 1234;

    public IDObjectTest() {
        idObject = new NonAbstractIDObject(id);
    }

    @Test
    public void testInitialization() {
        IDObject sut = new NonAbstractIDObject();
        assertTrue(sut.getId() >= 0 && sut.getId() < Integer.MAX_VALUE);
    }

    @Test
    public void testGetId() {
        assertEquals(id, idObject.getId());
    }

    @Test
    public void testSetId() {
        int newId = 5678;
        idObject.setId(newId);
        assertEquals(newId, idObject.getId());
    }

    @Test
    public void testRandomizeID() {
        int initialId = idObject.getId();
        idObject.randomizeID();
        int newId = idObject.getId();

        // Check that the ID has changed
        assertNotEquals(initialId, newId);

        // Check that the ID is non-negative
        assertTrue(idObject.getId() >= 0 && idObject.getId() < Integer.MAX_VALUE);
    }

    @Test
    public void testInsert() {
        // This cannot be tested without a database connection
        // --> Integration test
    }
}
