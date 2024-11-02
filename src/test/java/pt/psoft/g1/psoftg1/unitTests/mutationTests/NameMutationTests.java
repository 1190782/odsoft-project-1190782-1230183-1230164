package pt.psoft.g1.psoftg1.unitTests.mutationTests;

import org.junit.jupiter.api.Test;
import pt.psoft.g1.psoftg1.shared.model.Name;

import static org.junit.jupiter.api.Assertions.*;

public class NameMutationTests {

    @Test
    void testValidNameCreation() {
        // Given a valid name
        String validName = "JohnDoe123";

        // When creating a new Name object
        Name name = new Name(validName);

        // Then it should be created successfully
        assertEquals(validName, name.toString());
    }

    @Test
    void testCreationWithNullName() {
        // Given a null name
        String nullName = null;

        // When creating a new Name, expect an exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Name(nullName);
        });

        // Then the exception message should indicate the issue
        assertEquals("Name cannot be null", exception.getMessage());
    }

    @Test
    void testCreationWithBlankName() {
        // Given a blank name
        String blankName = "   "; // Only whitespace

        // When creating a new Name, expect an exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Name(blankName);
        });

        // Then the exception message should indicate the issue
        assertEquals("Name cannot be blank, nor only white spaces", exception.getMessage());
    }

    @Test
    void testCreationWithNonAlphanumericName() {
        // Given a name with non-alphanumeric characters
        String invalidName = "InvalidName!"; // Contains a special character

        // When creating a new Name, expect an exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Name(invalidName);
        });

        // Then the exception message should indicate the issue
        assertEquals("Name can only contain alphanumeric characters", exception.getMessage());
    }

    @Test
    void testSetNameWithNull() {
        // Given a valid name and then trying to set it to null
        Name name = new Name("ValidName");

        // When setting the name to null, expect an exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            name.setName(null);
        });

        // Then the exception message should indicate the issue
        assertEquals("Name cannot be null", exception.getMessage());
    }

    @Test
    void testSetNameWithBlank() {
        // Given a valid name and then trying to set it to blank
        Name name = new Name("ValidName");

        // When setting the name to blank, expect an exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            name.setName("    "); // Only whitespace
        });

        // Then the exception message should indicate the issue
        assertEquals("Name cannot be blank, nor only white spaces", exception.getMessage());
    }

    @Test
    void testSetNameWithNonAlphanumeric() {
        // Given a valid name and then trying to set it to a non-alphanumeric value
        Name name = new Name("ValidName");

        // When setting the name to a non-alphanumeric value, expect an exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            name.setName("Invalid!Name");
        });

        // Then the exception message should indicate the issue
        assertEquals("Name can only contain alphanumeric characters", exception.getMessage());
    }

    // Mutation Test 1: Change the condition in setName to only check for null
    @Test
    void testSetNameWithNullCheckOnly() {
        // Given a valid name
        Name name = new Name("JohnDoe");

        // When setting a blank name
        // This should throw an IllegalArgumentException since we are simulating a mutation
        // that allows blank names (i.e., the current implementation does not allow it)
        assertThrows(IllegalArgumentException.class, () -> {
            name.setName("   "); // Attempt to set a blank name
        }, "Expected IllegalArgumentException for blank name");
    }

    // Mutation Test 2: Change the exception type to a different type
    @Test
    void testSetNameWithDifferentExceptionType() {
        // Given a valid name
        Name name = new Name("JohnDoe");

        // When setting a non-alphanumeric name, we expect a RuntimeException (simulating mutation)
        Exception exception = assertThrows(RuntimeException.class, () -> {
            name.setName("Invalid!Name");
        });

        // Then we can check that some exception was thrown,
        // but we don't care about the specific message in this case.
        assertNotNull(exception, "Expected a RuntimeException");
    }
}
