package utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidatorUtilTest {

    @Test
    public void testIsValidEmail() {
        String validEmail = "email@example.com";
        boolean result = ValidatorUtil.isValidEmail(validEmail);
        assertTrue(result);
    }

    @Test
    public void isInvalidEmail() {
        String invalidEmail = "email@.com";
        boolean result = ValidatorUtil.isValidEmail(invalidEmail);
        assertFalse(result);
    }

    @Test
    public void testIsValidPassword() {
        String validPassword = "password123";
        boolean result = ValidatorUtil.isValidPassword(validPassword);
        assertTrue(result);
    }

    @Test
    public void isInvalidPassword() {
        String invalidPassword = "invalidPassword";
        boolean result = ValidatorUtil.isValidPassword(invalidPassword);
        assertFalse(result);
    }

    @Test
    public void testIsValidPhoneNumber() {
        String validPhoneNumber = "+610412345678";
        boolean result = ValidatorUtil.isValidPhoneNumber(validPhoneNumber);
        assertTrue(result);
    }

    @Test
    public void isInvalidPhoneNumber() {
        String invalidPhoneNumber = "invalidPhoneNumber";
        boolean result = ValidatorUtil.isValidPhoneNumber(invalidPhoneNumber);
        assertFalse(result);
    }
}
