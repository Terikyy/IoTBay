package utils;

import model.users.*;

public class UserUtil {

    /**
     * Authenticate the user based on email and password.
     * Replace this placeholder logic with actual database queries.
     *
     * @param email    The user's email.
     * @param password The user's password.
     * @return A User object if authentication is successful, null otherwise.
     */
    public static User authenticateUser(String email, String password) {
        // Placeholder: Replace with actual database query to fetch user details
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
        // Placeholder: Replace with actual database logic to save customer details
        int uid = (int) (Math.random() * 1000000); // Placeholder: Random ID for demo purposes
        // TODO: Add database logic to check if the email already exists and insert the new customer

        // For now, assume registration is always successful
        return new Customer(uid, name, email, password);
    }

    /**
     * Determine the user type based on the User object.
     *
     * @param user The User object.
     * @return The user type as a string ("Admin", "Staff", "Customer", or "Unknown").
     */
    public static String getUserType(User user) {
        if (user instanceof Admin) {
            return "Admin";
        } else if (user instanceof Staff) {
            return "Staff";
        } else if (user instanceof Customer) {
            return "Customer";
        }
        return "Unknown";
    }
}