package pt.psoft.g1.psoftg1.unitTests.mutationTests;

import org.junit.jupiter.api.Test;
import pt.psoft.g1.psoftg1.bookmanagement.model.Description;

import static org.junit.jupiter.api.Assertions.*;

public class DescriptionMutationTest {

    @Test
    void mutationTestSetNullDescription() {
        // Arrange
        Description description = new Description("Valid description");

        // Act
        description.setDescription(null);

        // Assert
        assertNull(description.toString(), "Expected description to be null when set to null");
    }

    @Test
    void mutationTestSetEmptyDescription() {
        // Arrange
        Description description = new Description("Initial description");

        // Act
        description.setDescription("");

        // Assert
        assertNull(description.toString(), "Expected description to be null when set to an empty string");
    }

    @Test
    void mutationTestSetValidDescription() {
        // Arrange
        String validDesc = "This is a valid description.";
        Description description = new Description(null);

        // Act
        description.setDescription(validDesc);

        // Assert
        assertEquals(validDesc, description.toString(), "Expected description to match the valid input");
    }

    @Test
    void mutationTestSetTooLongDescription() {
        // Arrange
        Description description = new Description("Short description");

        // Act & Assert
        String longDescription = "a".repeat(5000); // Create a long string of 5000 characters
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> description.setDescription(longDescription),
                "Expected IllegalArgumentException when setting a description longer than 4096 characters");

        // Additional assertion to ensure the exception message matches expected content
        assertTrue(thrown.getMessage().contains("maximum of 4096 characters"),
                "Expected exception message to indicate character limit exceeded");
    }
}
