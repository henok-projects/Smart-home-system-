package com.galsie.gcs.users;

import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.users.data.discrete.editprofilephoto.EditProfilePhotoErrorType;
import com.galsie.gcs.users.data.dto.editprofilephoto.request.EditUserProfilePhotoRequestDTO;
import com.galsie.gcs.users.data.dto.editprofilephoto.response.EditUserProfilePhotoResponseDTO;
import com.galsie.gcs.users.service.EditUserProfilePhotoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EditUserProfilePhotoServiceTest {

    private EditUserProfilePhotoService editUserProfilePhotoService;
    private EditUserProfilePhotoRequestDTO validRequest;
    private EditUserProfilePhotoRequestDTO invalidRequest;

    @BeforeEach
    public void setUp() {
        editUserProfilePhotoService = new EditUserProfilePhotoService();
        validRequest = new EditUserProfilePhotoRequestDTO();
        validRequest.setProfilePhoto("ValidBase64String");
        invalidRequest = new EditUserProfilePhotoRequestDTO();
        invalidRequest.setProfilePhoto("InvalidBase64String");
    }

//    @Test
//    public void testRequestUpdateUserProfilePhotoSuccess() {
//        GCSResponse<EditUserProfilePhotoResponseDTO> response =
//                editUserProfilePhotoService.requestUpdateUserProfilePhoto(validRequest);
//
//        assertEquals(EditProfilePhotoErrorType.INVALID_PHOTO_DATA, response.getResponseData().getEditProfilePhotoErrorType());
//    }

//    @Test
//    public void testRequestUpdateUserProfilePhotoErrorInvalidData() {
//        GCSResponse<EditUserProfilePhotoResponseDTO> response =
//                editUserProfilePhotoService.requestUpdateUserProfilePhoto(invalidRequest);
//        assertEquals(EditProfilePhotoErrorType.PHOTO_SIZE_EXCEEDS_10MB, response.getResponseData().getEditProfilePhotoErrorType());
//    }

    @Test
    public void testRequestUpdateUserProfilePhotoErrorSizeExceeds10MB() {
        // Set an empty photo to simulate exceeding size
        validRequest.setProfilePhoto("");
        GCSResponse<EditUserProfilePhotoResponseDTO> response =
                editUserProfilePhotoService.requestUpdateUserProfilePhoto(validRequest);

        assertEquals(EditProfilePhotoErrorType.PHOTO_SIZE_EXCEEDS_10MB, response.getResponseData().getEditProfilePhotoErrorType());
    }

//    @Test
//    public void testRequestUpdateUserProfilePhotoErrorNotSquare() {
//        // Set an empty photo to simulate a non-square photo
//        validRequest.setProfilePhoto("");
//        GCSResponse<EditUserProfilePhotoResponseDTO> response =
//                editUserProfilePhotoService.requestUpdateUserProfilePhoto(validRequest);
//
//        assertEquals(EditProfilePhotoErrorType.PHOTO_MUST_BE_A_SQUARE, response.getResponseData().getEditProfilePhotoErrorType());
//    }

//    @Test
//    public void testRequestRemoveProfilePhoto_Success() {
//        GCSResponse<EditUserProfilePhotoResponseDTO> response =
//                editUserProfilePhotoService.requestRemoveProfilePhoto();
//
//        assertEquals(null, response.getResponseData().getEditProfilePhotoErrorType());
//    }

//    @Test
//    public void testRequestRemoveProfilePhoto_Error_PhotoDoesNotExist() {
//        // Set the user account to have no profile photo
//        validRequest.setProfilePhoto("");
//        GCSResponse<EditUserProfilePhotoResponseDTO> response =
//                editUserProfilePhotoService.requestRemoveProfilePhoto();
//        assertEquals(EditProfilePhotoErrorType.PHOTO_DO_NOT_EXIST_FOR_THIS_USER, response.getResponseData().getEditProfilePhotoErrorType());
//    }
}
