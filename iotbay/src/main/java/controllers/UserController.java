package controllers;

import model.users.*;

public class UserController {
    /** This class is responsible for handling user-related operations
     * such as user registration, login, permission management, and profile management.
     * It is also responsible for authentication and authorization of users.
     */

    /**
     * Authenticate the user based on email and password.
     * Replace this placeholder logic with actual database queries.
     *
     * @param email    The user's email.
     * @param password The user's password.
     * @return A User object if authentication is successful, null otherwise.
     */
    public static User authenticateUser(String email, String password) {
        // Placeholder: Replace with actual database query to fetch user details (UserDAO)
        int uid = (int) (Math.random() * 1000000); // Placeholder: Random ID for demo purposes - will be replaced with UUID logic or database ID in production
        String name = "User Name"; // Placeholder name
        if ("admin@example.com".equals(email) && "admin123".equals(password)) {
            return new Admin(uid, name, email, password);
        } else if ("staff@example.com".equals(email) && "staff123".equals(password)) {
            return new Staff(uid, name, email, password);
        } else if ("customer@example.com".equals(email) && "customer123".equals(password)) {
            return new Customer(uid, name, email, password);
        }
        return null; // Invalid credentials
    }

    /**
     * Register a new customer.
     * Replace this placeholder logic with actual database insertion.
     *
     * @param name     The customer's name.
     * @param email    The customer's email.
     * @param password The customer's password.
     * @return A Customer object if registration is successful, null otherwise.
     */
    public static Customer registerCustomer(String name, String email, String password) {
        // Placeholder: Replace with actual database logic to save customer details (UserDAO)
        int uid = (int) (Math.random() * 1000000); // Placeholder: Random ID for demo purposes
        
        // For now, assume registration is always successful
        return new Customer(uid, name, email, password);
    }

    // TODO: Implement methods for user profile management, and logging of login/logout actions
}
