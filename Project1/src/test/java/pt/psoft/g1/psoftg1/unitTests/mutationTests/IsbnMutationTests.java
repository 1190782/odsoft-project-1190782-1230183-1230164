package pt.psoft.g1.psoftg1.unitTests.mutationTests;

import org.junit.jupiter.api.Test;
import pt.psoft.g1.psoftg1.bookmanagement.model.Isbn;

import static org.junit.jupiter.api.Assertions.*;

public class IsbnMutationTests {

    @Test
    void mutationTestNullIsbnThrowsException() {
        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new Isbn(null),
                "Expected IllegalArgumentException when ISBN is null");
        assertEquals("Isbn cannot be null", thrown.getMessage());
    }

    @Test
    void mutationTestInvalidIsbnLengthThrowsException() {
        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new Isbn("12345"),
                "Expected IllegalArgumentException for invalid ISBN length");
        assertEquals("Invalid ISBN-13 format or check digit.", thrown.getMessage());
    }

    @Test
    void mutationTestInvalidIsbn10FormatThrowsException() {
        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new Isbn("1234567890"),
                "Expected IllegalArgumentException for invalid ISBN-10 format");
        assertEquals("Invalid ISBN-13 format or check digit.", thrown.getMessage());
    }

    @Test
    void mutationTestInvalidIsbn13FormatThrowsException() {
        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new Isbn("9781234567890"),
                "Expected IllegalArgumentException for invalid ISBN-13 format");
        assertEquals("Invalid ISBN-13 format or check digit.", thrown.getMessage());
    }

    @Test
    void mutationTestValidIsbn10() {
        // Act
        Isbn isbn = new Isbn("0306406152");

        // Assert
        assertEquals("0306406152", isbn.toString(), "Expected valid ISBN-10 to be stored correctly");
    }

    @Test
    void mutationTestValidIsbn13() {
        // Act
        Isbn isbn = new Isbn("9780306406157");

        // Assert
        assertEquals("9780306406157", isbn.toString(), "Expected valid ISBN-13 to be stored correctly");
    }

    @Test
    void mutationTestInvalidIsbn10CheckDigit() {
        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new Isbn("0306406151"),
                "Expected IllegalArgumentException for incorrect check digit in ISBN-10");
        assertEquals("Invalid ISBN-13 format or check digit.", thrown.getMessage());
    }

    @Test
    void mutationTestInvalidIsbn13CheckDigit() {
        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new Isbn("9780306406156"),
                "Expected IllegalArgumentException for incorrect check digit in ISBN-13");
        assertEquals("Invalid ISBN-13 format or check digit.", thrown.getMessage());
    }
}
