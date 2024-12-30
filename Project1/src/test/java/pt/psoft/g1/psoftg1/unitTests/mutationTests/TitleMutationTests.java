package pt.psoft.g1.psoftg1.unitTests.mutationTests;

import org.junit.jupiter.api.Test;
import pt.psoft.g1.psoftg1.bookmanagement.model.Title;

import static org.junit.jupiter.api.Assertions.*;

public class TitleMutationTests {

    @Test
    void ensureNullTitleThrowsException() {
        Title title = new Title("Valid Title"); // Create a valid Title instance first
        assertThrows(IllegalArgumentException.class, () -> title.setTitle(null),
                "Expected IllegalArgumentException when title is null");
    }

    @Test
    void ensureBlankTitleThrowsException() {
        Title title = new Title("Valid Title");
        assertThrows(IllegalArgumentException.class, () -> title.setTitle("  "),
                "Expected IllegalArgumentException when title is blank");
    }

    @Test
    void ensureTitleTooLongThrowsException() {
        Title title = new Title("Valid Title");
        String longTitle = "A".repeat(129); // Create a title longer than TITLE_MAX_LENGTH
        assertThrows(IllegalArgumentException.class, () -> title.setTitle(longTitle),
                "Expected IllegalArgumentException when title exceeds max length");
    }

    @Test
    void ensureValidTitleDoesNotThrow() {
        Title title = new Title("Valid Title"); // This should work fine
        assertDoesNotThrow(() -> title.setTitle("Another Valid Title"),
                "Expected no exception when setting a valid title");
    }

    @Test
    void ensureSettingTitleStripsWhitespace() {
        Title title = new Title("   Valid Title   ");
        title.setTitle("   New Title   ");
        assertEquals("New Title", title.toString(),
                "Expected title to be set without leading or trailing whitespace");
    }

    @Test
    void ensureTitleCannotBeSetToExistingInvalidValue() {
        Title title = new Title("Valid Title");
        title.setTitle("Another Valid Title"); // Set to a valid title
        assertThrows(IllegalArgumentException.class, () -> title.setTitle(""),
                "Expected IllegalArgumentException when setting an invalid title (blank)");
        assertThrows(IllegalArgumentException.class, () -> title.setTitle(null),
                "Expected IllegalArgumentException when setting an invalid title (null)");
        String longTitle = "A".repeat(129); // Longer than max length
        assertThrows(IllegalArgumentException.class, () -> title.setTitle(longTitle),
                "Expected IllegalArgumentException when setting an invalid title (too long)");
    }
}
