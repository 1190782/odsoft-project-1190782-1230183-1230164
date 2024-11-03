package pt.psoft.g1.psoftg1.integrationTests.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.authormanagement.services.AuthorService;
import pt.psoft.g1.psoftg1.authormanagement.services.CreateAuthorRequest;
import pt.psoft.g1.psoftg1.authormanagement.services.UpdateAuthorRequest;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.shared.model.Photo;
import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Based on https://www.baeldung.com/spring-boot-testing
 * <p>Adaptations to Junit 5 with ChatGPT
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AuthorServiceImplIntegrationTest {
    @Autowired
    private AuthorService authorService;

    @MockBean
    private AuthorRepository authorRepository;
    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private PhotoRepository photoRepository;

    private Author author;

    @BeforeEach
    public void setUp() {
        author = new Author("Alex", "Author biography", null);

        when(authorRepository.findByAuthorNumber(1L)).thenReturn(Optional.of(author));
        when(authorRepository.findAll()).thenReturn(List.of(author));
        when(authorRepository.save(any(Author.class))).thenReturn(author);
    }

    @Test
    public void testFindAll() {
        Iterable<Author> authors = authorService.findAll();
        List<Author> authorList = new ArrayList<>();
        authors.forEach(authorList::add);  // Convert Iterable to List
        Assertions.assertEquals(1, authorList.size());
        assertThat(authorList.get(0).getName()).isEqualTo("Alex");
    }

    @Test
    public void testFindByAuthorNumber() {
        Optional<Author> foundAuthor = authorService.findByAuthorNumber(1L);
        assertThat(foundAuthor).isPresent();
        assertThat(foundAuthor.get().getName()).isEqualTo("Alex");
    }

    @Test
    public void testFindByName() {
        when(authorRepository.searchByNameNameStartsWith("Al")).thenReturn(List.of(author));
        List<Author> authors = authorService.findByName("Al");
        Assertions.assertEquals(1, authors.size());
        assertThat(authors.get(0).getName()).isEqualTo("Alex");
    }

    @Test
    public void testCreateAuthor() {
        CreateAuthorRequest request = new CreateAuthorRequest("Alex", "Author biography", null, null);
        Author createdAuthor = authorService.create(request);
        assertThat(createdAuthor).isNotNull();
        assertThat(createdAuthor.getName()).isEqualTo("Alex");
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    public void testFindBooksByAuthorNumber() {

        String validIsbn = "9782826012092";
        String validTitle = "Valid Book Title";
        String validDescription = "A descriptive book description.";
        Genre validGenre = new Genre("Fantasy");
        Author validAuthor = new Author("Author Name", "Bio of the author", null);
        List<Author> validAuthors = List.of(validAuthor);

        Book book = new Book(validIsbn, validTitle, validDescription, validGenre, validAuthors, null);
        when(bookRepository.findBooksByAuthorNumber(1L)).thenReturn(List.of(book));
        List<Book> books = authorService.findBooksByAuthorNumber(1L);
        assertEquals(1, books.size());
        assertEquals(validTitle, books.get(0).getTitle().toString());

    }

    @Test
    public void testFindCoAuthorsByAuthorNumber() {
        Author coAuthor = new Author("Co-Author", "Co-Author bio", null);
        when(authorRepository.findCoAuthorsByAuthorNumber(1L)).thenReturn(List.of(coAuthor));
        List<Author> coAuthors = authorService.findCoAuthorsByAuthorNumber(1L);
        assertEquals(1, coAuthors.size());
        assertThat(coAuthors.get(0).getName()).isEqualTo("Co-Author");
    }
}
