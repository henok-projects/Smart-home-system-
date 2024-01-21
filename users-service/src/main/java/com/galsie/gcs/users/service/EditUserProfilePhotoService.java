package com.galsie.gcs.users.service;

import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.exception.GCSResponseException;
import com.galsie.gcs.users.configuration.security.contexthelper.UserGalSecurityContextHelper;
import com.galsie.gcs.users.data.discrete.editprofilephoto.EditProfilePhotoErrorType;
import com.galsie.gcs.users.data.discrete.removeProfilePhoto.RemoveProfilePhotoErrorType;
import com.galsie.gcs.users.data.dto.editprofilephoto.request.EditUserProfilePhotoRequestDTO;
import com.galsie.gcs.users.data.dto.editprofilephoto.response.EditUserProfilePhotoResponseDTO;
import com.galsie.gcs.users.data.dto.removeprofilephoto.response.RemoveUserProfilePhotoResponseDTO;
import com.galsie.gcs.users.data.entity.UserAccountEntity;
import com.galsie.gcs.users.data.entity.UserProfilePhotoEntity;
import com.galsie.gcs.users.repository.UserAccountRepository;
import com.galsie.gcs.users.repository.UserProfilePhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EditUserProfilePhotoService {

    @Autowired
    UserGalSecurityContextHelper userGalSecurityContextHelper;
    @Autowired
    UserProfilePhotoRepository userProfilePhotoRepository;
    @Autowired
    UserAccountRepository userAccountRepository;


    public GCSResponse<EditUserProfilePhotoResponseDTO> requestUpdateUserProfilePhoto(EditUserProfilePhotoRequestDTO request) {
        try {
            return gcsInternalRequestUpdateUserProfilePhoto(request);
        } catch (GCSResponseException e) {
            return e.getGcsResponse(EditUserProfilePhotoResponseDTO.class);
        }
    }

    private GCSResponse<EditUserProfilePhotoResponseDTO> gcsInternalRequestUpdateUserProfilePhoto(EditUserProfilePhotoRequestDTO request) {

        var error = request.validateProfilePhoto();
        var error1 = request.validateProfilePhotoSizeInMB();
        var error2 = request.validateProfilePhotoAspectRation();
        if (error.isPresent()) {

            return EditUserProfilePhotoResponseDTO.responseError(EditProfilePhotoErrorType.INVALID_PHOTO_DATA);
        }
        if (error1.isPresent()) {

            return EditUserProfilePhotoResponseDTO.responseError(EditProfilePhotoErrorType.PHOTO_SIZE_EXCEEDS_10MB);
        }
        if (error2.isPresent()) {
            return EditUserProfilePhotoResponseDTO.responseError(EditProfilePhotoErrorType.PHOTO_MUST_BE_A_SQUARE);

        }
        var userId = userGalSecurityContextHelper.getUserAuthSession().getUserId();
        var updatedProfilePhoto = updateProfilePhoto(userId, request.getProfilePhoto());
        if (updatedProfilePhoto) {
            return EditUserProfilePhotoResponseDTO.responseSuccess();
        }
        return EditUserProfilePhotoResponseDTO.responseError(EditProfilePhotoErrorType.INVALID_PHOTO_DATA);

    }
    public GCSResponse<RemoveUserProfilePhotoResponseDTO> requestRemoveProfilePhoto() {
        try {
            return gcsInternalRequestRemoveUserProfilePhoto();
        } catch (GCSResponseException e) {
            return e.getGcsResponse(RemoveUserProfilePhotoResponseDTO.class);
        }
    }
    private GCSResponse<RemoveUserProfilePhotoResponseDTO> gcsInternalRequestRemoveUserProfilePhoto() {

        var userId = userGalSecurityContextHelper.getAuthenticatedUserInfo().getUserAuthSessionEntity().getUser().getId();
        var removeProfilePhoto = removeProfilePhoto(userId);
        if (removeProfilePhoto) {
            return RemoveUserProfilePhotoResponseDTO.responseSuccess();
        }
        return RemoveUserProfilePhotoResponseDTO.responseError(RemoveProfilePhotoErrorType.PHOTO_DOES_NOT_EXIST_FOR_THIS_USER);

    }


    private boolean updateProfilePhoto(long userId, String profilePhoto) {
        Optional<UserAccountEntity> userAccountEntity = userAccountRepository.findById(userId);
        if (userAccountEntity.isPresent()) {
            var userAccount = userAccountEntity.get();

            UserProfilePhotoEntity userProfilePhoto = userAccount.getUserProfilePhoto();

            if (userProfilePhoto != null) {
                // UserProfilePhotoEntity exists, update it
                userProfilePhoto.setContent(profilePhoto.getBytes());
                userProfilePhotoRepository.save(userProfilePhoto);
            } else {
                // UserProfilePhotoEntity doesn't exist, create a new one
                UserProfilePhotoEntity newProfilePhoto = new UserProfilePhotoEntity();
                newProfilePhoto.setUser(userAccount);
                newProfilePhoto.setContent(profilePhoto.getBytes());

                // Save the new UserProfilePhotoEntity
                userProfilePhotoRepository.save(newProfilePhoto);

                // Update the UserAccountEntity with the new UserProfilePhotoEntity
                userAccount.setUserProfilePhoto(newProfilePhoto);
            }

            // Save the updated UserAccountEntity
            userAccountRepository.save(userAccount);
            return true;
        }
        return false;
    }
    private boolean removeProfilePhoto(long userId) {
        Optional<UserAccountEntity> userAccountEntity = userAccountRepository.findById(userId);
        if (userAccountEntity.isPresent()) {
            UserAccountEntity userAccount = userAccountEntity.get();
            UserProfilePhotoEntity userProfilePhoto = userAccount.getUserProfilePhoto();

            if (userProfilePhoto != null) {
                // Delete the UserProfilePhotoEntity
                userProfilePhotoRepository.delete(userProfilePhoto);

                // Set the userProfilePhoto reference in UserAccountEntity to null
                userAccount.setUserProfilePhoto(null);

                // Save the updated UserAccountEntity
                userAccountRepository.save(userAccount);

                return true;
            }
        }
        return false;
    }

}
