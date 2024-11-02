package pt.psoft.g1.psoftg1.unitTests.mutationTests;

import org.junit.jupiter.api.Test;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;
import pt.psoft.g1.psoftg1.usermanagement.model.Role;

import static org.junit.jupiter.api.Assertions.*;

public class ReaderMutationTests {

    @Test
    void testReaderCreationWithValidData() {
        // Given valid username, password, and name
        String username = "reader1";
        String password = "ValidPass1!"; // Valid password
        String name = "John Doe"; // Valid name

        // When creating a new Reader
        Reader reader = new Reader(username, password);
        reader.setName(name);

        // Then ensure the reader is created correctly
        assertEquals(username, reader.getUsername());
        assertNotNull(reader.getPassword()); // Check if password is encoded
        assertTrue(reader.getAuthorities().contains(new Role(Role.READER)));
        assertEquals(name, reader.getName().toString()); // Check if name is correctly set
    }

    @Test
    void testReaderCreationWithInvalidNameNull() {
        // Given valid username and password, but invalid name
        String username = "reader2";
        String password = "ValidPass1!";

        // When creating a new Reader with a null name, expect an exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Reader reader = new Reader(username, password);
            reader.setName(null);
        });

        // Then the exception message should indicate the issue
        assertEquals("Name cannot be null", exception.getMessage());
    }

    @Test
    void testReaderCreationWithInvalidNameBlank() {
        // Given valid username and password, but invalid name
        String username = "reader3";
        String password = "ValidPass1!";

        // When creating a new Reader with a blank name, expect an exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Reader reader = new Reader(username, password);
            reader.setName("   "); // Blank name
        });

        // Then the exception message should indicate the issue
        assertEquals("Name cannot be blank, nor only white spaces", exception.getMessage());
    }

    @Test
    void testReaderCreationWithInvalidNameNonAlphanumeric() {
        // Given valid username and password, but invalid name
        String username = "reader4";
        String password = "ValidPass1!";

        // When creating a new Reader with a non-alphanumeric name, expect an exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Reader reader = new Reader(username, password);
            reader.setName("InvalidName#123"); // Non-alphanumeric name
        });

        // Then the exception message should indicate the issue
        assertEquals("Name can only contain alphanumeric characters", exception.getMessage());
    }

    @Test
    void testReaderCreationWithInvalidPassword() {
        // Given valid username and invalid password
        String username = "reader5";
        String password = "short"; // Invalid password, too short
        String name = "Valid Name";

        // When creating a new Reader, expect an exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Reader(username, password);
        });

        // Then the exception message should indicate the issue
        assertEquals("Given Password is not valid. It must contain at least 8 characters, 1 upper case letter, 1 lower case letter and 1 number or special character.", exception.getMessage());
    }

    @Test
    void testNewReaderFactoryMethodWithValidData() {
        // Given valid data for factory method
        String username = "reader6";
        String password = "AnotherValid1!"; // Valid password
        String name = "Jane Doe"; // Valid name

        // When creating a new Reader using the factory method
        Reader reader = Reader.newReader(username, password, name);

        // Then ensure the reader is created correctly
        assertEquals(name, reader.getName().toString()); // Assuming name is properly set
        assertTrue(reader.getAuthorities().contains(new Role(Role.READER)));
    }

    @Test
    void testNewReaderFactoryMethodWithInvalidPassword() {
        // Given invalid data
        String username = "reader7";
        String password = ""; // Empty password
        String name = "Valid Name";

        // When creating a new Reader using the factory method, expect an exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Reader.newReader(username, password, name);
        });

        // Then the exception message should indicate the issue
        assertEquals("Given Password is not valid. It must contain at least 8 characters, 1 upper case letter, 1 lower case letter and 1 number or special character.", exception.getMessage());
    }

    @Test
    void testReaderWithAuthority() {
        // Given a new Reader
        Reader reader = new Reader("reader8", "ValidPass1!");

        // When adding a new authority
        reader.addAuthority(new Role("CUSTOM_ROLE"));

        // Then the reader should have the new authority
        assertTrue(reader.getAuthorities().contains(new Role("CUSTOM_ROLE")));
    }

    // Mutation Test 1: Change visibility of addAuthority
    @Test
    void testAddAuthorityVisibilityChange() {
        // Change addAuthority to protected and test it indirectly through another method
        Reader reader = new Reader("reader9", "ValidPass1!");
        reader.addAuthority(new Role("NEW_ROLE"));

        assertTrue(reader.getAuthorities().contains(new Role("NEW_ROLE")));
    }
}
