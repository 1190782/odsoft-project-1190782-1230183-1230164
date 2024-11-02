package pt.psoft.g1.psoftg1.unitTests.mutationTests;

import org.hibernate.StaleObjectStateException;
import org.junit.jupiter.api.Test;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.readermanagement.model.PhoneNumber;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class LendingMutationTests {

    private final Genre validGenre = new Genre("Fiction");
    private final Author validAuthor = new Author("Author Name", "Author Bio", null);
    private final List<Author> validAuthors = Collections.singletonList(validAuthor);

    // Valid ISBN
    private final Book validBook = new Book("9783161484100", "Valid Title", "Valid Description", validGenre, validAuthors, null);

    // Updated ReaderDetails initialization using PhoneNumber
    private final Reader validReader = new Reader("John Doe", "ValidPassword1!"); // Assuming valid password
    private final ReaderDetails validReaderDetails = new ReaderDetails(
            1,
            validReader,
            "1990-01-01", // birthDate
            "912345678", // Valid phone number as string, will be converted in ReaderDetails
            true, // gdpr
            false, // marketing
            false, // thirdParty
            null, // photoURI
            Collections.emptyList() // interestList
    );

    private final int validSequential = 1;
    private final int validLendingDuration = 14; // days
    private final int validFineValuePerDayInCents = 50; // cents

    @Test
    void mutationTestNullBookThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Lending(null, validReaderDetails, validSequential, validLendingDuration, validFineValuePerDayInCents),
                "Expected IllegalArgumentException when book is null");
    }

    @Test
    void mutationTestNullReaderDetailsThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Lending(validBook, null, validSequential, validLendingDuration, validFineValuePerDayInCents),
                "Expected IllegalArgumentException when readerDetails is null");
    }

    @Test
    void mutationTestNegativeSequentialThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Lending(validBook, validReaderDetails, -1, validLendingDuration, validFineValuePerDayInCents),
                "Expected IllegalArgumentException when sequential number is negative");
    }

    @Test
    void mutationTestSetReturnedWhenAlreadyReturnedThrowsException() {
        Lending lending = new Lending(validBook, validReaderDetails, validSequential, validLendingDuration, validFineValuePerDayInCents);
        lending.setReturned(lending.getVersion(), "Returned in good condition");

        assertThrows(IllegalArgumentException.class, () -> lending.setReturned(lending.getVersion(), "Another commentary"),
                "Expected IllegalArgumentException when attempting to set returned date again");
    }

    @Test
    void mutationTestSetReturnedWithVersionMismatchThrowsException() {
        Lending lending = new Lending(validBook, validReaderDetails, validSequential, validLendingDuration, validFineValuePerDayInCents);
        assertThrows(StaleObjectStateException.class, () -> lending.setReturned(lending.getVersion() + 1, "Commentary"),
                "Expected StaleObjectStateException when version does not match");
    }

    @Test
    void mutationTestGetDaysDelayedOnTimeReturnReturnsZero() {
        Lending lending = new Lending(validBook, validReaderDetails, validSequential, validLendingDuration, validFineValuePerDayInCents);
        // Assert that the lending is not overdue
        assertEquals(0, lending.getDaysDelayed(), "Expected days delayed to be zero when book is not yet overdue");
    }

    @Test
    void mutationTestDaysUntilReturnWhenNotReturned() {
        Lending lending = new Lending(validBook, validReaderDetails, validSequential, validLendingDuration, validFineValuePerDayInCents);
        // Since the book is not returned, days until return should be valid
        assertNotNull(lending.getDaysUntilReturn(), "Expected days until return to be present when book is not returned");
    }
}
