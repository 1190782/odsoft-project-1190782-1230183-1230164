package pt.psoft.g1.psoftg1.unitTests.mutationTests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import pt.psoft.g1.psoftg1.readermanagement.model.BirthDate;

import java.time.LocalDate;

public class BirthDateMutationTests {
    private BirthDate validBirthDate;
    private static final int VALID_YEAR = 2000;
    private static final int VALID_MONTH = 5;
    private static final int VALID_DAY = 15;

    @BeforeEach
    void setUp() {
        validBirthDate = new BirthDate(VALID_YEAR, VALID_MONTH, VALID_DAY);
    }

    @Test
    void testConstructorWithValidDate() {
        assertEquals("2000-5-15", validBirthDate.toString());
    }

    @Test
    void testConstructorWithInvalidDateFormat() {
        assertThrows(IllegalArgumentException.class, () -> new BirthDate("15-05-2000"),
                "Expected an IllegalArgumentException for invalid date format");
    }

    @Test
    void testConstructorWithFutureDate() {
        // Assuming minimumAge is set to 18, this date should throw an AccessDeniedException
        assertThrows(AccessDeniedException.class, () -> new BirthDate(2025, 5, 15),
                "Expected an AccessDeniedException for a date in the future");
    }

    @Test
    void testConstructorWithMinimumAgeDate() {
        // Assuming minimumAge is set to 18
        LocalDate minimumAgeDate = LocalDate.now().minusYears(18);
        BirthDate birthDate = new BirthDate(minimumAgeDate.getYear(), minimumAgeDate.getMonthValue(), minimumAgeDate.getDayOfMonth());

        // Expecting the output without leading zeros
        String expectedDateString = String.format("%d-%d-%d", minimumAgeDate.getYear(), minimumAgeDate.getMonthValue(), minimumAgeDate.getDayOfMonth());

        assertEquals(expectedDateString, birthDate.toString(),
                "BirthDate should be valid for users exactly at the minimum age");
    }

    @Test
    void testToString() {
        assertEquals("2000-5-15", validBirthDate.toString());
    }
}
