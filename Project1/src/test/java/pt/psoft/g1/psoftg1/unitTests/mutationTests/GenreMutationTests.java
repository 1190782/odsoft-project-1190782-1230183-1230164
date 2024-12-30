package pt.psoft.g1.psoftg1.unitTests.mutationTests;

import org.junit.jupiter.api.Test;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GenreMutationTests {

    @Test
    void mutationTestNullGenreThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Genre(null),
                "Expected IllegalArgumentException when genre is null");
    }

    @Test
    void mutationTestBlankGenreThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Genre(" "),
                "Expected IllegalArgumentException when genre is blank");
    }

    @Test
    void mutationTestTooLongGenreThrowsException() {
        String longGenre = "A".repeat(101); // 101 characters long
        assertThrows(IllegalArgumentException.class, () -> new Genre(longGenre),
                "Expected IllegalArgumentException when genre exceeds maximum length");
    }

    @Test
    void mutationTestValidGenreCreation() {
        String validGenreName = "Fantasy";
        Genre genre = new Genre(validGenreName);
        assertEquals(validGenreName, genre.toString(), "Expected genre to be set correctly");
    }

    @Test
    void mutationTestGenreWithExactMaxLength() {
        String maxLengthGenre = "A".repeat(100); // 100 characters long
        Genre genre = new Genre(maxLengthGenre);
        assertEquals(maxLengthGenre, genre.toString(), "Expected genre to be set correctly with max length");
    }

}
