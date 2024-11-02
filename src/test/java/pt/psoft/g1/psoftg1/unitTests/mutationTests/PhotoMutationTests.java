package pt.psoft.g1.psoftg1.unitTests.mutationTests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import pt.psoft.g1.psoftg1.shared.model.Photo;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PhotoMutationTests {

    @Test
    void testConstructorWithValidPath() {
        // Valid Path
        Path validPath = Paths.get("path/to/photo.jpg");
        Photo photo = new Photo(validPath);

        assertEquals(validPath.toString(), photo.getPhotoFile(), "Photo file path should match the given path");
    }

    @Test
    void testConstructorWithNullPath() {
        // Test with a null Path
        assertThrows(NullPointerException.class, () -> {
            new Photo(null);
        }, "Expected NullPointerException for null Path");
    }

    @Test
    void testSetPhotoFileWithValidFilePath() {
        // Valid file path
        Photo photo = new Photo(Paths.get("initial/path/to/photo.jpg"));
        String newPath = "new/path/to/photo.jpg";
        photo.setPhotoFile(newPath);

        assertEquals(newPath, photo.getPhotoFile(), "Photo file path should be updated to the new path");
    }



    @Test
    void testSetPhotoFileWithNull() {
        // Test setting photoFile to null
        Photo photo = new Photo(Paths.get("initial/path/to/photo.jpg"));

        // Set to null
        photo.setPhotoFile(null);

        // Expect photoFile to be null
        assertNull(photo.getPhotoFile(), "Expected photoFile to be null after setting it to null");
    }

    @Test
    void testSetPhotoFileWithEmptyString() {
        // Test setting photoFile to an empty string
        Photo photo = new Photo(Paths.get("initial/path/to/photo.jpg"));

        // Set to empty string
        photo.setPhotoFile(""); // This should now set the photoFile to an empty string

        // Expect photoFile to be empty
        assertEquals("", photo.getPhotoFile(), "Expected photoFile to be empty after setting it to an empty string");
    }

    @Test
    void testPhotoFileIsSetCorrectly() {
        // Test setting a valid photoFile
        Photo photo = new Photo(Paths.get("initial/path/to/photo.jpg"));
        String validPhotoPath = "valid/photo/path.jpg";

        // Set a valid path
        photo.setPhotoFile(validPhotoPath);

        // Check if the photoFile is set correctly
        assertEquals(validPhotoPath, photo.getPhotoFile(), "Expected photoFile to be set to the valid path");
    }
}
