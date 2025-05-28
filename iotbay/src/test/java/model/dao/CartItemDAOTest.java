package model.dao;

import model.Product;
import model.lineproducts.CartItem;
import model.users.Customer;
import model.users.User;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CartItemDAOTest {

    private static Connection conn;
    private CartItemDAO cartItemDAO;
    private ProductDAO productDAO;
    private UserDAO userDAO;

    private final int TEST_PRODUCT_ID = Integer.MAX_VALUE;
    private final int TEST_USER_ID = Integer.MAX_VALUE;
    private final int TEST_QUANTITY = 3;

    @BeforeAll
    public static void setupClass() throws SQLException, ClassNotFoundException {
        // Get a connection to the database
        DBConnector connector = new DBConnector();
        conn = connector.openConnection();

        // Disable auto-commit to use transactions
        conn.setAutoCommit(false);
    }

    @BeforeEach
    public void setup() throws SQLException {
        // Initialize DAOs with the shared connection
        cartItemDAO = new CartItemDAO(conn);
        productDAO = new ProductDAO(conn);
        userDAO = new UserDAO(conn);

        // Ensure test product exists
        try {
            productDAO.findById(TEST_PRODUCT_ID);
        } catch (SQLException e) {
            // Create test product if it doesn't exist
            Product testProduct = new Product(TEST_PRODUCT_ID, "Test Product", "Test Description",
                    99.99, 100, new Date(System.currentTimeMillis()), "Test Category");
            productDAO.insert(testProduct);
        }

        // Ensure test user exists
        try {
            userDAO.findById(TEST_USER_ID);
        } catch (SQLException e) {
            // Create test user if it doesn't exist
            User testUser = new Customer(TEST_USER_ID, "Test User", "test@example.com", "password", null, true);
            userDAO.insert(testUser);
        }

        // Clean up any existing test cart items
        try {
            cartItemDAO.deleteByUserAndProduct(TEST_USER_ID, TEST_PRODUCT_ID);
        } catch (SQLException e) {
            // Ignore if test cart item doesn't exist
        }
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Rollback all changes made during the test
        conn.rollback();
    }

    @AfterAll
    public static void tearDownClass() throws SQLException {
        // Close the connection
        if (conn != null && !conn.isClosed()) {
            conn.setAutoCommit(true);  // Reset to default
            conn.close();
        }
    }

    // US401: Add IoT devices to shopping cart
    @Test
    @Order(1)
    public void testInsert() throws SQLException {
        // Arrange
        CartItem cartItem = new CartItem(TEST_PRODUCT_ID, TEST_USER_ID, TEST_QUANTITY);

        // Act
        int result = cartItemDAO.insert(cartItem);

        // Assert
        assertTrue(result > 0, "Insert should affect at least one row");

        // Verify the cart item was actually inserted
        CartItem inserted = cartItemDAO.findByUserAndProduct(TEST_USER_ID, TEST_PRODUCT_ID);
        assertNotNull(inserted, "Cart item should be found after insertion");
        assertEquals(TEST_QUANTITY, inserted.getQuantity(), "Cart item quantity should match");
    }

    // US402 & US403: View shopping cart and total cost
    @Test
    @Order(2)
    public void testGetAllByUserId() throws SQLException {
        // Arrange - Create a test cart item first
        CartItem cartItem = new CartItem(TEST_PRODUCT_ID, TEST_USER_ID, TEST_QUANTITY);
        cartItemDAO.insert(cartItem);

        // Act
        List<CartItem> cartItems = cartItemDAO.getAllByUserId(TEST_USER_ID);

        // Assert
        assertNotNull(cartItems, "Cart items list should not be null");
        assertFalse(cartItems.isEmpty(), "Cart items list should not be empty");

        boolean found = cartItems.stream()
                .anyMatch(item -> item.getProductID() == TEST_PRODUCT_ID &&
                        item.getUserId() == TEST_USER_ID &&
                        item.getQuantity() == TEST_QUANTITY);

        assertTrue(found, "The created test cart item should be in the user's cart");
    }

    // US404: Change amount of an IoT device in shopping cart
    @Test
    @Order(3)
    public void testUpdate() throws SQLException {
        // Arrange - Create a test cart item first
        CartItem cartItem = new CartItem(TEST_PRODUCT_ID, TEST_USER_ID, TEST_QUANTITY);
        cartItemDAO.insert(cartItem);

        // Act - Update the cart item quantity
        int updatedQuantity = 5;
        CartItem updatedCartItem = new CartItem(TEST_PRODUCT_ID, TEST_USER_ID, updatedQuantity);

        int result = cartItemDAO.update(updatedCartItem);

        // Assert
        assertTrue(result > 0, "Update should affect at least one row");

        // Verify the cart item was actually updated
        CartItem updated = cartItemDAO.findByUserAndProduct(TEST_USER_ID, TEST_PRODUCT_ID);
        assertNotNull(updated, "Cart item should be found after update");
        assertEquals(updatedQuantity, updated.getQuantity(), "Cart item quantity should be updated");
    }

    // US405: Remove an IoT device from shopping cart
    @Test
    @Order(4)
    public void testDeleteByUserAndProduct() throws SQLException {
        // Arrange - Create a test cart item first
        CartItem cartItem = new CartItem(TEST_PRODUCT_ID, TEST_USER_ID, TEST_QUANTITY);
        cartItemDAO.insert(cartItem);

        // Act
        int result = cartItemDAO.deleteByUserAndProduct(TEST_USER_ID, TEST_PRODUCT_ID);

        // Assert
        assertTrue(result > 0, "Delete should affect at least one row");

        // Verify the cart item was actually deleted
        CartItem deleted = cartItemDAO.findByUserAndProduct(TEST_USER_ID, TEST_PRODUCT_ID);
        assertNull(deleted, "Cart item should not be found after deletion");
    }

    // Additional test for clearing cart functionality
    @Test
    @Order(5)
    public void testDeleteAllByUserId() throws SQLException {
        // Arrange - Create multiple test cart items for the user
        CartItem cartItem1 = new CartItem(TEST_PRODUCT_ID, TEST_USER_ID, TEST_QUANTITY);
        cartItemDAO.insert(cartItem1);

        // Create a second product and cart item if possible
        int secondProductId = TEST_PRODUCT_ID - 1;
        try {
            Product secondProduct = new Product(secondProductId, "Second Test Product",
                    "Another test product", 49.99, 50, new Date(System.currentTimeMillis()), "Test Category");
            productDAO.insert(secondProduct);

            CartItem cartItem2 = new CartItem(secondProductId, TEST_USER_ID, 2);
            cartItemDAO.insert(cartItem2);
        } catch (SQLException e) {
            // If we can't create a second product, just proceed with one
        }

        // Act
        int result = cartItemDAO.deleteAllByUserId(TEST_USER_ID);

        // Assert
        assertTrue(result > 0, "Delete all should affect at least one row");

        // Verify all cart items for the user were deleted
        List<CartItem> remainingItems = cartItemDAO.getAllByUserId(TEST_USER_ID);
        assertTrue(remainingItems.isEmpty(), "No cart items should remain after clearing the cart");
    }

    // Test for product-related cart operations
    @Test
    @Order(6)
    public void testGetAllByProductId() throws SQLException {
        // Arrange - Create a test cart item first
        CartItem cartItem = new CartItem(TEST_PRODUCT_ID, TEST_USER_ID, TEST_QUANTITY);
        cartItemDAO.insert(cartItem);

        // Act
        List<CartItem> cartItems = cartItemDAO.getAllByProductId(TEST_PRODUCT_ID);

        // Assert
        assertNotNull(cartItems, "Cart items list should not be null");
        assertFalse(cartItems.isEmpty(), "Cart items list should not be empty");

        boolean found = cartItems.stream()
                .anyMatch(item -> item.getProductID() == TEST_PRODUCT_ID &&
                        item.getUserId() == TEST_USER_ID);

        assertTrue(found, "The created test cart item should be found by product ID");
    }

    // Test for deleting all cart items for a product
    @Test
    @Order(7)
    public void testDeleteAllByProductId() throws SQLException {
        // Arrange - Create a test cart item first
        CartItem cartItem = new CartItem(TEST_PRODUCT_ID, TEST_USER_ID, TEST_QUANTITY);
        cartItemDAO.insert(cartItem);

        // Act
        int result = cartItemDAO.deleteAllByProductId(TEST_PRODUCT_ID);

        // Assert
        assertTrue(result > 0, "Delete all should affect at least one row");

        // Verify all cart items for the product were deleted
        List<CartItem> remainingItems = cartItemDAO.getAllByProductId(TEST_PRODUCT_ID);
        assertTrue(remainingItems.isEmpty(), "No cart items should remain after deleting by product ID");
    }
}