package utils;

import model.users.Admin;
import model.users.Customer;
import model.users.Staff;
import model.users.User;

public class UserUtil {

    /**
     * Determine the user type based on the User object.
     *
     * @param user The User object.
     * @return The user type as a string ("Admin", "Staff", "Customer", or "Unknown").
     */
    //TODO: determine if we need this (I would say we only need isAdmin and isStaff)
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

    /**
     * Check if the user is an admin.
     *
     * @param user The User object.
     * @return True if the user is an admin, false otherwise.
     */
    public static boolean isAdmin(User user) {
        return user instanceof Admin;
    }

    /**
     * Check if the user is a staff member.
     *
     * @param user The User object.
     * @return True if the user is an staff / admin, false otherwise.
     */
    public static boolean isStaff(User user) {
        return user instanceof Staff || user instanceof Admin;
    }
}