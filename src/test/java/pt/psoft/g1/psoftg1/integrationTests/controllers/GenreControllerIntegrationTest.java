package pt.psoft.g1.psoftg1.integrationTests.controllers;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import pt.psoft.g1.psoftg1.bookmanagement.services.GenreBookCountDTO;
import pt.psoft.g1.psoftg1.genremanagement.api.*;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreLendingsDTO;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreService;
import pt.psoft.g1.psoftg1.genremanagement.services.GetAverageLendingsQuery;
import pt.psoft.g1.psoftg1.shared.services.Page;
import pt.psoft.g1.psoftg1.shared.services.SearchRequest;

import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebMvcTest(GenreController.class)
@AutoConfigureMockMvc
class GenreControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenreService genreService;

    @MockBean // Mock the GenreViewMapper
    private GenreViewMapper genreViewMapper;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAverageLendings_ShouldReturnListOfGenreLendingsView() throws Exception {
        // Arrange
        SearchRequest<GetAverageLendingsQuery> request = new SearchRequest<>();


        GetAverageLendingsQuery query = new GetAverageLendingsQuery();
        query.setMonth(1);

        Page page = new Page();
        page.setNumber(1);
        page.setLimit(10);

        request.setPage(page);
        request.setQuery(query);


        GenreLendingsDTO lendingsDTO = new GenreLendingsDTO("Fiction", 5); // Example DTO
        List<GenreLendingsDTO> dtoList = Collections.singletonList(lendingsDTO);


        when(genreService.getAverageLendings(any(), any())).thenReturn(dtoList);


        List<GenreLendingsView> lendingsViewList = Collections.singletonList(new GenreLendingsView());
        lendingsViewList.get(0).setGenre("Fiction");
        lendingsViewList.get(0).setValue(5);


        when(genreViewMapper.toGenreAvgLendingsView(dtoList)).thenReturn(lendingsViewList);


        mockMvc.perform(post("/api/genres/avgLendingsPerGenre")
                        .with(csrf()) // Include CSRF token
                        .with(user("username").roles("USER")) // Mock authentication
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].genre", is("Fiction")))
                .andExpect(jsonPath("$.items[0].value", is(5)));
    }

    @Test
    void getTop_ShouldReturnListOfTopGenres() throws Exception {
        // Arrange
        GenreBookCountDTO dto1 = new GenreBookCountDTO("Fiction", 10L);
        GenreBookCountDTO dto2 = new GenreBookCountDTO("Science", 5L);
        List<GenreBookCountDTO> topGenresDTO = List.of(dto1, dto2);

        // Mock the service method to return DTOs
        when(genreService.findTopGenreByBooks()).thenReturn(topGenresDTO);

        // Create the expected GenreBookCountView objects using the mapper
        GenreView genreView1 = new GenreView();
        genreView1.setGenre("Fiction");

        GenreView genreView2 = new GenreView();
        genreView2.setGenre("Science");

        GenreBookCountView view1 = new GenreBookCountView();
        view1.setGenreView(genreView1);
        view1.setBookCount(10L);

        GenreBookCountView view2 = new GenreBookCountView();
        view2.setGenreView(genreView2);
        view2.setBookCount(5L);

        List<GenreBookCountView> expectedViews = List.of(view1, view2);

        // Mock the mapping
        when(genreViewMapper.toGenreBookCountView(topGenresDTO)).thenReturn(expectedViews);

        // Act & Assert
        mockMvc.perform(get("/api/genres/top5") // Using the imported get method
                        .with(csrf()) // Include CSRF token if needed
                        .with(user("username").roles("USER"))) // Mock authentication
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(2)))
                .andExpect(jsonPath("$.items[0].genreView.genre", is("Fiction")))
                .andExpect(jsonPath("$.items[0].bookCount", is(10)))
                .andExpect(jsonPath("$.items[1].genreView.genre", is("Science")))
                .andExpect(jsonPath("$.items[1].bookCount", is(5)));
    }

    @Test
    void getTop_ShouldReturnNotFound_WhenNoGenres() throws Exception {
        // Arrange: Mock the service method to return an empty list
        when(genreService.findTopGenreByBooks()).thenReturn(Collections.emptyList());

        // Act & Assert: Perform the request and expect a 404 Not Found
        mockMvc.perform(get("/api/genres/top5") // Ensure this is the correct import for get
                        .with(csrf()) // Include CSRF token if needed
                        .with(user("username").roles("USER"))) // Mock authentication
                .andExpect(status().isNotFound()) // Check for 404 status
                .andExpect(jsonPath("$.message", is("Not found"))) // Check for error message
                .andExpect(jsonPath("$.details[0]", is("No genres to show"))); // Check the detail message
    }


}
