package model.dao;

import model.Product;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductDAOTest {
    
    private static Connection conn;
    private ProductDAO productDAO;
    
    private final int TEST_PRODUCT_ID = Integer.MAX_VALUE;
    private final String TEST_PRODUCT_NAME = "Test Smart Device";
    private final String TEST_DESCRIPTION = "A test device for unit testing";
    private final double TEST_PRICE = 99.99;
    private final int TEST_STOCK = 50;
    private final Date TEST_RELEASE_DATE = new Date();
    private final String TEST_CATEGORY = "Test Category";
    
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
        // Initialize DAO with the shared connection
        productDAO = new ProductDAO(conn);
        
        // Clean up any existing test data
        try {
            productDAO.deleteById(TEST_PRODUCT_ID);
        } catch (SQLException e) {
            // Ignore if test product doesn't exist
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
    
    // US301: View IOT devices
    @Test
    @Order(1)
    public void testGetAll() throws SQLException {
        // Act
        List<Product> products = productDAO.getAll();
        
        // Assert
        assertNotNull(products, "Product list should not be null");
        // We can't assert exact count as database might have any number of products
        // but we can check that it doesn't throw an exception
    }
    
    // US302: Search for IOT devices by name
    @Test
    @Order(2)
    public void testSearchByName() throws SQLException {
        // Arrange - Create a test product first
        Product product = new Product(TEST_PRODUCT_ID, TEST_PRODUCT_NAME, TEST_DESCRIPTION, 
                TEST_PRICE, TEST_STOCK, TEST_RELEASE_DATE, TEST_CATEGORY);
        productDAO.insert(product);
        
        // Act - Search for the product by part of its name
        List<Product> results = productDAO.searchByName("Smart");
        
        // Assert
        assertNotNull(results, "Search results should not be null");
        boolean found = results.stream()
                .anyMatch(p -> p.getName().equals(TEST_PRODUCT_NAME));
        assertTrue(found, "The created test product should be found by search");
    }
    
    // US303: Search for IOT devices by category
    @Test
    @Order(3)
    public void testFindProductsByCategory() throws SQLException {
        // Arrange - Create a test product first
        Product product = new Product(TEST_PRODUCT_ID, TEST_PRODUCT_NAME, TEST_DESCRIPTION, 
                TEST_PRICE, TEST_STOCK, TEST_RELEASE_DATE, TEST_CATEGORY);
        productDAO.insert(product);
        
        // Act
        List<Product> results = productDAO.findProductsByCategory(TEST_CATEGORY);
        
        // Assert
        assertNotNull(results, "Category search results should not be null");
        boolean found = results.stream()
                .anyMatch(p -> p.getProductID() == TEST_PRODUCT_ID);
        assertTrue(found, "The created test product should be found by category search");
    }
    
    // US304: View details for an IOT device
    @Test
    @Order(4)
    public void testFindById() throws SQLException {
        // Arrange - Create a test product first
        Product product = new Product(TEST_PRODUCT_ID, TEST_PRODUCT_NAME, TEST_DESCRIPTION, 
                TEST_PRICE, TEST_STOCK, TEST_RELEASE_DATE, TEST_CATEGORY);
        productDAO.insert(product);
        
        // Act
        Product found = productDAO.findById(TEST_PRODUCT_ID);
        
        // Assert
        assertNotNull(found, "Product should be found by ID");
        assertEquals(TEST_PRODUCT_ID, found.getProductID(), "Product ID should match");
        assertEquals(TEST_PRODUCT_NAME, found.getName(), "Product name should match");
        assertEquals(TEST_DESCRIPTION, found.getDescription(), "Product description should match");
        assertEquals(TEST_PRICE, found.getPrice(), 0.001, "Product price should match");
        assertEquals(TEST_STOCK, found.getStock(), "Product stock should match");
        assertEquals(TEST_CATEGORY, found.getCategory(), "Product category should match");
    }
    
    // US701: View inventory
    @Test
    @Order(5)
    public void testGetAllCategories() throws SQLException {
        // Arrange - Create a test product with a distinct category
        Product product = new Product(TEST_PRODUCT_ID, TEST_PRODUCT_NAME, TEST_DESCRIPTION, 
                TEST_PRICE, TEST_STOCK, TEST_RELEASE_DATE, TEST_CATEGORY);
        productDAO.insert(product);
        
        // Act
        List<String> categories = productDAO.getAllCategories();
        
        // Assert
        assertNotNull(categories, "Categories list should not be null");
        assertTrue(categories.contains(TEST_CATEGORY), 
                "The category of the created test product should be in the list");
    }
    
    // US702: Add devices to inventory
    @Test
    @Order(6)
    public void testInsert() throws SQLException {
        // Arrange
        Product product = new Product(TEST_PRODUCT_ID, TEST_PRODUCT_NAME, TEST_DESCRIPTION, 
                TEST_PRICE, TEST_STOCK, TEST_RELEASE_DATE, TEST_CATEGORY);
        
        // Act
        int result = productDAO.insert(product);
        
        // Assert
        assertTrue(result > 0, "Insert should affect at least one row");
        
        // Verify the product was actually inserted
        Product inserted = productDAO.findById(TEST_PRODUCT_ID);
        assertNotNull(inserted, "Product should be found after insertion");
        assertEquals(TEST_PRODUCT_NAME, inserted.getName(), "Product name should match");
    }
    
    // US703 & US704: Update device details and inventory amount
    @Test
    @Order(7)
    public void testUpdate() throws SQLException {
        // Arrange - Create a test product first
        Product product = new Product(TEST_PRODUCT_ID, TEST_PRODUCT_NAME, TEST_DESCRIPTION, 
                TEST_PRICE, TEST_STOCK, TEST_RELEASE_DATE, TEST_CATEGORY);
        productDAO.insert(product);
        
        // Act - Update the product
        String updatedName = "Updated Test Device";
        String updatedDescription = "Updated test description";
        double updatedPrice = 149.99;
        int updatedStock = 75;
        String updatedCategory = "Updated Category";
        
        Product updatedProduct = new Product(TEST_PRODUCT_ID, updatedName, updatedDescription, 
                updatedPrice, updatedStock, TEST_RELEASE_DATE, updatedCategory);
        
        int result = productDAO.update(updatedProduct);
        
        // Assert
        assertTrue(result > 0, "Update should affect at least one row");
        
        // Verify the product was actually updated
        Product updated = productDAO.findById(TEST_PRODUCT_ID);
        assertNotNull(updated, "Product should be found after update");
        assertEquals(updatedName, updated.getName(), "Product name should be updated");
        assertEquals(updatedDescription, updated.getDescription(), "Product description should be updated");
        assertEquals(updatedPrice, updated.getPrice(), 0.001, "Product price should be updated");
        assertEquals(updatedStock, updated.getStock(), "Product stock should be updated");
        assertEquals(updatedCategory, updated.getCategory(), "Product category should be updated");
    }
    
    // US705: Remove device from inventory
    @Test
    @Order(8)
    public void testDeleteById() throws SQLException {
        // Arrange - Create a test product first
        Product product = new Product(TEST_PRODUCT_ID, TEST_PRODUCT_NAME, TEST_DESCRIPTION, 
                TEST_PRICE, TEST_STOCK, TEST_RELEASE_DATE, TEST_CATEGORY);
        productDAO.insert(product);
        
        // Act
        int result = productDAO.deleteById(TEST_PRODUCT_ID);
        
        // Assert
        assertTrue(result > 0, "Delete should affect at least one row");
        
        // Verify the product was actually deleted
        Product deleted = productDAO.findById(TEST_PRODUCT_ID);
        assertNull(deleted, "Product should not be found after deletion");
    }
}