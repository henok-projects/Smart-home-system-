package com.galsie.gcs.users.service;

import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.exception.GCSResponseException;
import com.galsie.gcs.users.data.discrete.getcontactinfo.GetContactInfoResponseErrorType;
import com.galsie.gcs.users.data.dto.getcontactinfo.request.common.*;
import com.galsie.gcs.users.data.dto.getcontactinfo.request.many.GetManyUserContactInfoRequestDTO;
import com.galsie.gcs.users.data.dto.getcontactinfo.request.single.GetUserContactInfoRequestDTO;
import com.galsie.gcs.users.data.dto.getcontactinfo.response.GetManyUserContactInfoResponseDTO;
import com.galsie.gcs.users.data.dto.getcontactinfo.response.GetUserContactInfoResponseDTO;
import com.galsie.gcs.users.data.dto.common.PhoneNumberDTO;
import com.galsie.gcs.users.data.dto.common.UserContactInfoDTO;
import com.galsie.gcs.users.data.entity.UserAccountEntity;
import com.galsie.gcs.users.repository.UserAccountRepository;
import com.galsie.gcs.users.repository.UserEmailRepository;
import com.galsie.gcs.users.repository.UserPhoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserContactInfoService {

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    UserPhoneRepository userPhoneRepository;

    @Autowired
    UserEmailRepository userEmailRepository;

    public GCSResponse<GetUserContactInfoResponseDTO> requestGetUserContactInfo(GetUserContactInfoRequestDTO request) {
        try {
            return gcsInternalRequestGetUserContactInfo(request);
        } catch (GCSResponseException e) {
            return e.getGcsResponse(GetUserContactInfoResponseDTO.class);
        }
    }

    public GCSResponse<GetUserContactInfoResponseDTO> gcsInternalRequestGetUserContactInfo(GetUserContactInfoRequestDTO request) {
        GetUserContactInfoResponseDTO response = new GetUserContactInfoResponseDTO();

        if (request != null && request.getUserReference() != null) {
            UserReferenceDTO userReference = request.getUserReference();
            UserContactInfoDTO userContactInfo = null;

            if (userReference instanceof UserReferenceByUsernameDTO userReferenceByUsernameDTO) {
                userContactInfo = getUserInfoByUsername(userReferenceByUsernameDTO.getUsername(), request.isIncludeProfilePhoto());
            } else if (userReference instanceof UserReferenceByEmailDTO userReferenceByEmailDTO) {
                userContactInfo = getUserInfoByEmail(userReferenceByEmailDTO.getEmail(), request.isIncludeProfilePhoto());
            } else if (userReference instanceof UserReferenceByPhoneDTO userReferenceByPhoneDTO) {
                UserReferenceByPhoneDTO byPhoneDTO = userReferenceByPhoneDTO;
                userContactInfo = getUserInfoByPhone(byPhoneDTO.getCountryCode(), byPhoneDTO.getPhone(), request.isIncludeProfilePhoto());
            } else if (userReference instanceof UserReferenceByIdDTO userReferenceById) {
                userContactInfo = getUserInfoById(userReferenceById.getUserId(), request.isIncludeProfilePhoto());
            }

            if (userContactInfo != null) {
                response.setUserContactInfo(userContactInfo);
                return GetUserContactInfoResponseDTO.responseSuccess(response.getUserContactInfo());
            } else {
                response.setGetContactInfoResponseError(GetContactInfoResponseErrorType.USER_NOT_FOUND);
                return GetUserContactInfoResponseDTO.responseError(response.getGetContactInfoResponseError());

            }
        } else {

            response.setGetContactInfoResponseError(GetContactInfoResponseErrorType.USER_NOT_FOUND);
            return GetUserContactInfoResponseDTO.responseError(response.getGetContactInfoResponseError());
        }

    }

    public GCSResponse<GetManyUserContactInfoResponseDTO> requestGetManyUserContactInfo(GetManyUserContactInfoRequestDTO request) {
        try {
            return gcsInternalRequestGetManyUserContactInfo(request);
        } catch (GCSResponseException e) {
            return e.getGcsResponse(GetManyUserContactInfoResponseDTO.class);
        }

    }

    public GCSResponse<GetManyUserContactInfoResponseDTO> gcsInternalRequestGetManyUserContactInfo(GetManyUserContactInfoRequestDTO request) {
        GetManyUserContactInfoResponseDTO response = new GetManyUserContactInfoResponseDTO();
        List<GetUserContactInfoResponseDTO> userContactInfoResponses = new ArrayList<>();

        var requestForUsers = request.getForUsers();

        if (request != null && request.getForUsers() != null && !request.getForUsers().isEmpty()) {
            for (UserReferenceDTO userReference : requestForUsers) {
                GetUserContactInfoRequestDTO userContactInfoRequest = new GetUserContactInfoRequestDTO();
                userContactInfoRequest.setUserReference(userReference);
                userContactInfoRequest.setIncludeProfilePhoto(request.isIncludeProfilePhotos());
                GetUserContactInfoResponseDTO userContactInfoResponse = gcsInternalRequestGetUserContactInfo(userContactInfoRequest).getResponseData();
                if (userContactInfoResponse != null) {
                    userContactInfoResponses.add(userContactInfoResponse);
                }
            }
        }

        return GetManyUserContactInfoResponseDTO.responseSuccess(userContactInfoResponses);
    }

    // ...
    private UserContactInfoDTO createUserContactInfoDTO(UserAccountEntity userAccountEntity, boolean isIncludeProfilePhoto) {
        UserContactInfoDTO userContactInfoDTO = new UserContactInfoDTO();
        userContactInfoDTO.setUsername(userAccountEntity.getUsername());
        if (userAccountEntity.getUserEmail() != null) {
            userContactInfoDTO.setEmail(userAccountEntity.getUserEmail().getEmail());
        }
        userContactInfoDTO.setUserId(userAccountEntity.getId());
        //check if fullname is available and add that
        userContactInfoDTO.setFullName(userAccountEntity.getUserInfo().getName());
        if (isIncludeProfilePhoto) {
            // Include profile photo in the response
            if (userAccountEntity.getUserProfilePhoto() != null && userAccountEntity.getUserProfilePhoto().getContent() != null) {
                userContactInfoDTO.setProfilePhoto(Arrays.toString(userAccountEntity.getUserProfilePhoto().getContent()));
            }
        }
        if (userAccountEntity.getUserPhone() != null) {
            userContactInfoDTO.setPhone(PhoneNumberDTO.builder()
                    .countryCode(String.valueOf(userAccountEntity.getUserPhone().getCountryCode()))
                    .number(userAccountEntity.getUserPhone().getPhoneNumber())
                    .build());
        }
        return userContactInfoDTO;
    }

    private UserContactInfoDTO getUserInfoByUsername(String username, boolean isIncludeProfilePhoto) {
        var userAccountEntity = userAccountRepository.findByUsername(username);
        if (userAccountEntity.isPresent()) {
            return createUserContactInfoDTO(userAccountEntity.get(), isIncludeProfilePhoto);
        } else {
            return null;
        }
    }

    private UserContactInfoDTO getUserInfoByEmail(String email, boolean isIncludeProfilePhoto) {
        var userEmailEntity = userEmailRepository.findByEmail(email);
        if (userEmailEntity.isPresent()) {
            return createUserContactInfoDTO(userEmailEntity.get().getUser(), isIncludeProfilePhoto);
        } else {
            return null;
        }
    }

    private UserContactInfoDTO getUserInfoByPhone(short countryCode, String phone, boolean isIncludeProfilePhoto) {
        var userPhoneEntity = userPhoneRepository.findByCountryCodeAndPhoneNumber(countryCode, phone);
        if (userPhoneEntity.isPresent()) {
            return createUserContactInfoDTO(userPhoneEntity.get().getUser(), isIncludeProfilePhoto);
        } else {
            return null;
        }
    }

    private UserContactInfoDTO getUserInfoById(Long userId, boolean isIncludeProfilePhoto) {
        var userAccountEntity = userAccountRepository.findById(userId);
        if (userAccountEntity.isPresent()) {
            return createUserContactInfoDTO(userAccountEntity.get(), isIncludeProfilePhoto);
        } else {
            return null;
        }
    }
}


