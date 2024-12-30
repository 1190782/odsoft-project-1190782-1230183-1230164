package pt.psoft.g1.psoftg1.unitTests.mutationTests;

import org.junit.jupiter.api.Test;
import pt.psoft.g1.psoftg1.lendingmanagement.model.LendingNumber;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

public class LendingNumberMutationTests {

    @Test
    void mutationTestNullLendingNumberThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new LendingNumber((String) null),
                "Expected IllegalArgumentException when lending number is null");
    }

    @Test
    void mutationTestInvalidFormatThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new LendingNumber("2024-23"),
                "Expected IllegalArgumentException when lending number format is invalid");
    }

    @Test
    void mutationTestInvalidYearThrowsException() {
        int futureYear = LocalDate.now().getYear() + 1; // Future year
        assertThrows(IllegalArgumentException.class, () -> new LendingNumber(futureYear, 5),
                "Expected IllegalArgumentException when year is in the future");
    }

    @Test
    void mutationTestNegativeSequentialThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new LendingNumber(2024, -1),
                "Expected IllegalArgumentException when sequential number is negative");
    }

    @Test
    void mutationTestValidLendingNumberCreation() {
        LendingNumber lendingNumber = new LendingNumber(2024, 23);
        assertEquals("2024/23", lendingNumber.toString(), "Expected valid lending number to be set correctly");
    }

    @Test
    void mutationTestValidLendingNumberFromString() {
        LendingNumber lendingNumber = new LendingNumber("2024/23");
        assertEquals("2024/23", lendingNumber.toString(), "Expected valid lending number from string to be set correctly");
    }

    @Test
    void mutationTestValidLendingNumberWithCurrentYear() {
        int currentYear = LocalDate.now().getYear();
        LendingNumber lendingNumber = new LendingNumber(1); // Sequential number
        assertEquals(currentYear + "/1", lendingNumber.toString(), "Expected valid lending number with current year");
    }

    @Test
    void mutationTestInvalidStringLengthThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new LendingNumber("2024/23/extra"),
                "Expected IllegalArgumentException when lending number string is too long");
    }
}
