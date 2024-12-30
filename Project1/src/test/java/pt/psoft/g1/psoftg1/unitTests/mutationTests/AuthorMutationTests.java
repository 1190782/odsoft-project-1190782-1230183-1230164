package pt.psoft.g1.psoftg1.unitTests.mutationTests;

import org.hibernate.StaleObjectStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.services.UpdateAuthorRequest;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import static org.junit.jupiter.api.Assertions.*;

class AuthorMutationTests {

    private Author author;

    @BeforeEach
    void setUp() {
        author = new Author("Initial Name", "Initial Bio", "initialPhotoURI");
    }

    @Test
    void ensureVersionMismatchThrowsInApplyPatch() {
        UpdateAuthorRequest request = new UpdateAuthorRequest("New Bio", "New Name", null, "newPhotoURI");
        assertThrows(StaleObjectStateException.class, () -> applyPatchWithNewVersion(request),
                "Expected StaleObjectStateException when version does not match");
    }



    @Test
    void ensureApplyPatchChangesNameIfNotNull() {
        UpdateAuthorRequest request = new UpdateAuthorRequest(null, "Updated Name", null, null);
        author.applyPatch(author.getVersion(), request);
        assertEquals("Updated Name", author.getName(), "Expected name to be updated in applyPatch");
    }

    @Test
    void ensureApplyPatchChangesBioIfNotNull() {
        UpdateAuthorRequest request = new UpdateAuthorRequest("Updated Bio", null, null, null);
        author.applyPatch(author.getVersion(), request);
        assertEquals("Updated Bio", author.getBio(), "Expected bio to be updated in applyPatch");
    }

    @Test
    void ensureApplyPatchUpdatesPhotoIfMultipartFileProvided() {
        // Test to handle `MultipartFile` and verify `setPhotoInternal` is called with correct data
        MockMultipartFile photoFile = new MockMultipartFile("photo", "photo.jpg", "image/jpeg", "sample data".getBytes());
        UpdateAuthorRequest request = new UpdateAuthorRequest(null, null, photoFile, null);

        author.applyPatch(author.getVersion(), request);

        // Assuming `EntityWithPhoto` has a `getPhoto()` or equivalent
        assertNotNull(author.getPhoto(), "Expected photo to be set in applyPatch");
    }

    @Test
    void ensureApplyPatchIgnoresNullFields() {
        UpdateAuthorRequest request = new UpdateAuthorRequest(null, null, null, null);
        author.applyPatch(author.getVersion(), request);

        assertEquals("Initial Name", author.getName(), "Expected name to remain unchanged");
        assertEquals("Initial Bio", author.getBio(), "Expected bio to remain unchanged");
    }

    @Test
    void ensureRemovePhotoThrowsOnVersionMismatch() {
        long mismatchedVersion = author.getVersion() + 1;
        assertThrows(ConflictException.class, () -> author.removePhoto(mismatchedVersion),
                "Expected ConflictException when provided version does not match");
    }

    @Test
    void ensureRemovePhotoClearsPhoto() {
        author.removePhoto(author.getVersion());

        // Assuming there's a way to check if `photo` is cleared or null in `EntityWithPhoto`
        assertNull(author.getPhoto(), "Expected photo to be null after removePhoto");
    }

    private void applyPatchWithNewVersion(UpdateAuthorRequest request) {
        author.applyPatch(author.getVersion() + 1, request);
    }

    private void removePhotoWithNewVersion(long newVersion) {
        author.removePhoto(newVersion);
    }
}
