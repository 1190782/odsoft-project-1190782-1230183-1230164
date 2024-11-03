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
import pt.psoft.g1.psoftg1.bookmanagement.services.SearchBooksQuery;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@SpringBootTest
public class BookCustomRepoIntegrationTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    private Author author;
    private Genre genre;
    private Book book;

    @BeforeEach
    public void setUp() {
        author = new Author("Manuel Antonio Pina",
                "Manuel António Pina foi um jornalista e escritor português, premiado em 2011 com o Prémio Camões", null);
        authorRepository.save(author);

        genre = new Genre("Fiction");
        genreRepository.save(genre);

        book = new Book("9782826012092",
                "O Inspetor Max",
                "A well-known German shepherd working for the judiciary",
                genre,
                List.of(author),
                null);
        bookRepository.save(book);
    }

    @AfterEach
    public void tearDown() {
        bookRepository.delete(book);
        genreRepository.delete(genre);
        authorRepository.delete(author);
    }

    @Test
    public void testSave() {
        Book newBook = new Book("9782826012092", "New Book Title", "Description of the new book", genre, List.of(author), null);
        Book savedBook = bookRepository.save(newBook);
        assertNotNull(savedBook);
        assertEquals(newBook.getIsbn(), savedBook.getIsbn());
        bookRepository.delete(savedBook);
    }

    @Test
    public void testFindByIsbn() {
        Optional<Book> foundBook = bookRepository.findByIsbn(book.getIsbn());
        assertThat(foundBook).isPresent();
        assertThat(foundBook.get().getIsbn()).isEqualTo(book.getIsbn());
    }

    @Test
    public void testFindByTitle() {
        List<Book> foundBooks = bookRepository.findByTitle("O Inspetor Max");
        assertThat(foundBooks).isNotEmpty();
        assertThat(foundBooks).contains(book);
    }

    @Test
    public void testFindByGenre() {
        List<Book> foundBooks = bookRepository.findByGenre(genre.getGenre());
        assertThat(foundBooks).isNotEmpty();
        assertThat(foundBooks).contains(book);
    }

    @Test
    public void testFindByAuthorName() {
        List<Book> foundBooks = bookRepository.findByAuthorName("Manuel Antonio Pina");
        assertThat(foundBooks).isNotEmpty();
        assertThat(foundBooks).contains(book);
    }

    @Test
    public void testFindBooksByAuthorNumber() {
        List<Book> foundBooks = bookRepository.findBooksByAuthorNumber(author.getAuthorNumber());
        assertThat(foundBooks).isNotEmpty();
        assertThat(foundBooks).contains(book);
    }

    @Test
    public void testSearchBooks() {
        SearchBooksQuery query = new SearchBooksQuery();
        query.setTitle("O Inspetor Max");
        query.setGenre("Fiction");
        query.setAuthorName("Manuel Antonio Pina");

        Page page = new Page(1, 10); // Assuming page 1 with limit of 10 items
        List<Book> foundBooks = bookRepository.searchBooks(page, query);

        assertThat(foundBooks).isNotEmpty();
        assertThat(foundBooks).contains(book);
    }

    @Test
    public void testSearchBooksWithEmptyCriteria() {
        SearchBooksQuery query = new SearchBooksQuery();
        query.setTitle("");
        query.setGenre("");
        query.setAuthorName("");

        Page page = new Page(1, 10); // Assuming page 1 with limit of 10 items
        List<Book> foundBooks = bookRepository.searchBooks(page, query);

        assertThat(foundBooks).isNotEmpty();
        // In this case, we expect to find the initial book added in setup.
        assertThat(foundBooks).contains(book);
    }
}
