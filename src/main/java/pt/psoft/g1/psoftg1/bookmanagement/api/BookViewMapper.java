package pt.psoft.g1.psoftg1.bookmanagement.api;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.shared.api.ViewMapper;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class BookViewMapper extends ViewMapper {
    @Mapping(target = "genre", source = "genre")
    @Mapping(target = "isbn", source = "isbn")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "title", source = "title")
    public abstract BookView toBookView(Book book);

    public abstract List<BookView> toBookView(List<Book> bookList);

    public String[] map(List<Author> authorList) {
        String[] authorNames = new String[authorList.size()];
        for(int i = 0; i < authorList.size(); i++) {
            authorNames[i] = authorList.get(i).getName().toString();
        }

        return authorNames;
    }
}
