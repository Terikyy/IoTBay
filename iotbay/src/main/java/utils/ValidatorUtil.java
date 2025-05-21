package utils;

public class ValidatorUtil {

    public static boolean isValidEmail(String mail) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return mail.matches(emailRegex);
    }

    public static boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$";
        return password.matches(passwordRegex);
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        String phoneRegex = "^$|^(\\+61|0)[2-478][0-9]{8}$";
        return phoneNumber.matches(phoneRegex);
    }
}
