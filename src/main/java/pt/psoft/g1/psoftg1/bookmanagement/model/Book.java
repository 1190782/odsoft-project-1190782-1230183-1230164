package pt.psoft.g1.psoftg1.bookmanagement.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.bookmanagement.services.UpdateBookRequest;


import java.util.ArrayList;
import java.util.List;

import static org.springdoc.core.service.GenericResponseService.setDescription;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames={"ISBN"})})
public class Book {
    @Getter
    @EmbeddedId
    @Column(name="ISBN")
    Isbn isbn;

    @Getter
    @Embedded
    @NotNull
    Title title;

    @Getter
    @ManyToOne
    @NotNull
    Genre genre;

    @Getter
    @OneToMany
    private List<Author> authors = new ArrayList<>();

    @Embedded
    @NotNull
    Description description;

    private void setTitle(String title) {this.title = new Title(title);}

    private void setIsbn(String isbn) {
        this.isbn = new Isbn(isbn);
    }

    private void setDescription(String description) {this.description = new Description(description); }

    private void setGenre(Genre genre) {this.genre = genre; }

    private void setAuthors(List<Author> authors) {this.authors = authors; }

    public Book(String isbn, String title, Genre genre,List<Author> authors) {
        setTitle(title);
        setIsbn(isbn);
        setGenre(genre);
        setAuthors(authors);
    }

    public Book(String isbn, String title, String description, Genre genre, List<Author> authors) {
        setTitle(title);
        setIsbn(isbn);
        setDescription(description);
        setGenre(genre);
        setAuthors(authors);
    }

    protected Book() {
        // got ORM only
    }

    public void applyPatch(UpdateBookRequest request) {
        String title = request.getTitle();
        String description = request.getDescription();
        Genre genre = request.getGenreObj();
        List<Author> authors = request.getAuthorObjList();

        if(title != null) {
            setTitle(title);
        }

        if(description != null) {
            setDescription(description);
        }

        if(genre != null) {
            setGenre(genre);
        }

        if(authors != null) {
            setAuthors(authors);
        }
    }
}
