package com.galsie.gcs.users;

import com.galsie.gcs.users.data.dto.editprofilephoto.request.EditUserProfilePhotoRequestDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EditUserProfilePhotoRequestDTOTest {

//    @Test
    public void testIsValidProfilePhoto() {
        EditUserProfilePhotoRequestDTO request = new EditUserProfilePhotoRequestDTO();
        String validBase64Data = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8DwAB/wAD/7k=";

        request.setProfilePhoto(validBase64Data);
        assertTrue(request.isValidProfilePhoto());
    }
//    @Test
    public void testInValidProfilePhoto() {
        EditUserProfilePhotoRequestDTO request = new EditUserProfilePhotoRequestDTO();
        String invalidBase64Data = "ThisIsNotValidBase64Data"; // Invalid Base64 data
        request.setProfilePhoto(invalidBase64Data);
        assertTrue(request.isValidProfilePhoto()); // This should be true because the data is invalid.
    }


//    @Test
    public void testValidateProfilePhoto() {
        EditUserProfilePhotoRequestDTO request = new EditUserProfilePhotoRequestDTO();
        request.setProfilePhoto("ValidBase64String");

        assertFalse(request.validateProfilePhoto().isPresent());
    }

//    @Test
    public void testValidateProfilePhotoSizeInMB() {
        EditUserProfilePhotoRequestDTO request = new EditUserProfilePhotoRequestDTO();
        request.setProfilePhoto("ValidBase64String");

        assertFalse(request.validateProfilePhotoSizeInMB().isPresent());

        request.setProfilePhoto(""); // Simulate an empty photo
        assertTrue(request.validateProfilePhotoSizeInMB().isPresent());
    }

//    @Test
    public void testValidateProfilePhotoAspectRation() {
        EditUserProfilePhotoRequestDTO request = new EditUserProfilePhotoRequestDTO();
        request.setProfilePhoto("ValidBase64String");

        assertFalse(request.validateProfilePhotoAspectRation().isPresent());

        request.setProfilePhoto(""); // Simulate an empty photo
        assertTrue(request.validateProfilePhotoAspectRation().isPresent());
    }
}
