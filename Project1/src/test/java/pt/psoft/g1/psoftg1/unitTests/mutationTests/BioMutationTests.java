package pt.psoft.g1.psoftg1.unitTests.mutationTests;

import pt.psoft.g1.psoftg1.authormanagement.model.Bio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BioMutationTests {



    private static final int BIO_MAX_LENGTH = 4096;

    @BeforeEach
    void setUp() {
        // No setup needed; tests will directly verify constructor and method behavior
    }

    @Test
    void mutationTestSkipNullCheck() {
        // Test for mutation: if null-check is bypassed
        Bio bio = new Bio("initial bio");
        assertThrows(IllegalArgumentException.class, () -> bio.setBio(null),
                "Expected IllegalArgumentException if null-check in setBio is bypassed");
    }

    @Test
    void mutationTestSkipBlankCheck() {
        // Test for mutation: if blank-check is bypassed
        Bio bio = new Bio("initial bio");
        assertThrows(IllegalArgumentException.class, () -> bio.setBio("   "),
                "Expected IllegalArgumentException if blank-check in setBio is bypassed");
    }

    @Test
    void mutationTestChangeMaxLengthCondition() {
        // Test for mutation: if max length condition is altered
        Bio bio = new Bio("initial bio");
        String tooLongBio = "a".repeat(4097); // one character over the max
        assertThrows(IllegalArgumentException.class, () -> bio.setBio(tooLongBio),
                "Expected IllegalArgumentException if length condition in setBio is altered");
    }
}
