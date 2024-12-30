package pt.psoft.g1.psoftg1.integrationTests.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.*;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class BookServiceImplTest {

    @Autowired
    private BookService bookService;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private GenreRepository genreRepository;
    @MockBean
    private AuthorRepository authorRepository;
    private Book book;
    private Author author;
    private Genre genre;

    @BeforeEach
    public void setUp() {
        genre = new Genre("Fiction");

        author = new Author("Author Name", "Biography", null);

        book = new Book("9782826012092", "Book Title", "Description", genre, List.of(author), null);

        when(bookRepository.findByIsbn("9782826012092")).thenReturn(Optional.of(book));
        when(genreRepository.findByString("Fiction")).thenReturn(Optional.of(genre));
        when(authorRepository.findByAuthorNumber(1L)).thenReturn(Optional.of(author));
    }

    @Test
    public void testCreateBookThrowsConflictExceptionIfBookExists() {
        CreateBookRequest request = new CreateBookRequest();
        when(bookRepository.findByIsbn("9782826012092")).thenReturn(Optional.of(book));

        assertThrows(ConflictException.class, () -> bookService.create(request, "9782826012092"));
    }


    @Test
    public void testFindByGenre() {
        when(bookRepository.findByGenre("Fiction")).thenReturn(List.of(book));

        List<Book> books = bookService.findByGenre("Fiction");

        assertEquals(1, books.size());
        assertEquals("Fiction", books.get(0).getGenre().toString());
    }

    @Test
    public void testFindByTitle() {
        when(bookRepository.findByTitle("Book Title")).thenReturn(List.of(book));

        List<Book> books = bookService.findByTitle("Book Title");

        assertEquals(1, books.size());
        assertEquals("Book Title", books.get(0).getTitle().toString());
    }

    @Test
    public void testFindByAuthorName() {
        when(bookRepository.findByAuthorName("Author%")).thenReturn(List.of(book));

        List<Book> books = bookService.findByAuthorName("Author");

        assertEquals(1, books.size());
        assertEquals("Author Name", books.get(0).getAuthors().get(0).getName());
    }

    @Test
    public void testFindByIsbn() {
        Book foundBook = bookService.findByIsbn("9782826012092");

        assertEquals("9782826012092", foundBook.getIsbn());
    }
    @Test
    public void testSearchBooks() {
        Page page = new Page(1, 10);
        SearchBooksQuery query = new SearchBooksQuery("Title", "Author", "Genre");
        when(bookRepository.searchBooks(any(Page.class), any(SearchBooksQuery.class))).thenReturn(List.of(book));

        List<Book> result = bookService.searchBooks(page, query);

        assertEquals(1, result.size());
        assertEquals("Book Title", result.get(0).getTitle().toString());
    }
}
