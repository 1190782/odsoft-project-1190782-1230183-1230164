package pt.psoft.g1.psoftg1.unitTests.mutationTests;

import org.junit.jupiter.api.Test;
import pt.psoft.g1.psoftg1.readermanagement.model.PhoneNumber;

import static org.junit.jupiter.api.Assertions.*;

public class PhoneNumberMutationTests {

    void testValidPhoneNumberStartsWith9() {
        PhoneNumber phoneNumber = new PhoneNumber("912345678");
        assertEquals("912345678", phoneNumber.toString());
    }

    // Test valid phone number starting with 2
    @Test
    void testValidPhoneNumberStartsWith2() {
        PhoneNumber phoneNumber = new PhoneNumber("234567891");
        assertEquals("234567891", phoneNumber.toString());
    }

    // Test invalid phone number that is too short
    @Test
    void testInvalidPhoneNumberTooShort() {
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber("91234567"));
    }

    // Test invalid phone number that is too long
    @Test
    void testInvalidPhoneNumberTooLong() {
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber("9123456789"));
    }

    // Test invalid phone number that does not start with 9 or 2
    @Test
    void testInvalidPhoneNumberDoesNotStartWith9Or2() {
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber("312345678"));
    }

    // Test mutation by changing valid phone number to invalid
    @Test
    void testMutationInvalidPhoneNumber() {
        // Assume we modify the PhoneNumber class to accept any string
        // This mutation will test if the original validation logic is working
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new PhoneNumber("312345678"));
        assertEquals("Phone number is not valid: 312345678", exception.getMessage());
    }

    // Test null phone number input
    @Test
    void testNullPhoneNumber() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new PhoneNumber(null);
        });
    }
    // Test empty phone number input
    @Test
    void testEmptyPhoneNumber() {
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber(""));
    }
}
