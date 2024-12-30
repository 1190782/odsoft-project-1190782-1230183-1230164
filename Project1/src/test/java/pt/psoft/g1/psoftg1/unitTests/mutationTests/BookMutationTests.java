package pt.psoft.g1.psoftg1.unitTests.mutationTests;

import org.junit.jupiter.api.Test;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.services.UpdateBookRequest;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookMutationTests {

    private final String validIsbn = "9782826012092";
    private final String validTitle = "Valid Book Title";
    private final String validDescription = "A descriptive book description.";
    private final Genre validGenre = new Genre("Fantasy");
    private final Author validAuthor = new Author("Author Name", "Bio of the author", null);
    private final List<Author> validAuthors = List.of(validAuthor);

    @Test
    void mutationTestGenreNullCheck() {
        // Verifies if genre null check is bypassed in constructor
        assertThrows(IllegalArgumentException.class, () -> new Book(validIsbn, validTitle, validDescription, null, validAuthors, null),
                "Expected IllegalArgumentException if genre null-check is bypassed");
    }

    @Test
    void mutationTestAuthorsNullCheck() {
        // Verifies if authors null check is bypassed in constructor
        assertThrows(IllegalArgumentException.class, () -> new Book(validIsbn, validTitle, validDescription, validGenre, null, null),
                "Expected IllegalArgumentException if authors null-check is bypassed");
    }

    @Test
    void mutationTestAuthorsEmptyCheck() {
        assertThrows(IllegalArgumentException.class, this::createBookWithEmptyAuthors,
                "Expected IllegalArgumentException if authors empty check is bypassed");
    }

    @Test
    void mutationTestUpdateTitleThroughApplyPatch() {
        // Verifies if title is correctly updated through applyPatch when request provides a new title
        Book book = new Book(validIsbn, validTitle, validDescription, validGenre, validAuthors, null);
        UpdateBookRequest request = new UpdateBookRequest();
        request.setTitle("New Title");

        book.applyPatch(book.getVersion(), request);

        // Since there's no direct title getter, re-checking requires manual constructor comparison
        Book updatedBook = new Book(validIsbn, "New Title", validDescription, validGenre, validAuthors, null);
        assertEquals(updatedBook.getTitle().toString(), book.getTitle().toString(), "Expected title to be updated to 'New Title'");
    }

    private void createBookWithEmptyAuthors() {
        new Book(validIsbn, validTitle, validDescription, validGenre, List.of(), null);
    }
}
