package pt.psoft.g1.psoftg1.integrationTests.repository;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
public class BookRepositoryIntegrationTest {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private GenreRepository genreRepository;

    private Book book;
    private Author author;
    private Genre genre;

    @BeforeEach
    public void setUp() {
        // Setup a genre
        genre = new Genre("Fiction");
        genreRepository.save(genre);

        // Setup an author
        author = new Author("JK Rowling",
                "British author, best known for the Harry Potter series.",
                null);
        authorRepository.save(author);

        // Setup a book
        List<Author> authors = new ArrayList<>();
        authors.add(author);
        book = new Book("9780747532743", "Harry Potter and the Philosopher's Stone",
                "A young wizard's journey begins.",
                genre, authors, null);
        bookRepository.save(book);
    }

    @AfterEach
    public void tearDown() {
        bookRepository.delete(book);
        authorRepository.delete(author);
        genreRepository.delete(genre);
    }

    @Test
    public void testSave() {
        Book newBook = new Book("9782826012092", "Test Book",
                "This is a test description.",
                genre, List.of(author), null);
        Book savedBook = bookRepository.save(newBook);
        assertNotNull(savedBook);
        assertEquals(newBook.getIsbn(), savedBook.getIsbn());
        bookRepository.delete(savedBook); // Clean up after test
    }

    @Test
    public void testFindByIsbn() {
        Optional<Book> foundBook = bookRepository.findByIsbn(book.getIsbn());
        assertTrue(foundBook.isPresent());
        assertEquals(book.getTitle(), foundBook.get().getTitle());
    }

    @Test
    public void testFindByTitle() {
        List<Book> foundBooks = bookRepository.findByTitle(book.getTitle().getTitle());
        assertFalse(foundBooks.isEmpty());
        assertEquals(book.getTitle(), foundBooks.get(0).getTitle());
    }

    @Test
    public void testFindByAuthorName() {
        List<Book> foundBooks = bookRepository.findByAuthorName(author.getName());
        assertFalse(foundBooks.isEmpty());
        assertEquals(book.getTitle(), foundBooks.get(0).getTitle());
    }

    @Test
    public void testFindByGenre() {
        List<Book> foundBooks = bookRepository.findByGenre(genre.toString());
        assertFalse(foundBooks.isEmpty());
        assertEquals(book.getTitle(), foundBooks.get(0).getTitle());
    }

    @Test
    public void testFindBooksByAuthorNumber() {
        // Assuming the Author has an ID assigned
        Long authorNumber = author.getAuthorNumber(); // Assuming 'getPk()' returns the ID
        List<Book> books = bookRepository.findBooksByAuthorNumber(authorNumber);
        assertFalse(books.isEmpty());
        assertEquals(book.getTitle(), books.get(0).getTitle());
    }
}
