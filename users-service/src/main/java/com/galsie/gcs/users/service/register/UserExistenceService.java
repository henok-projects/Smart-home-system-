package com.galsie.gcs.users.service.register;

import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.users.data.dto.registration.request.checks.CheckExistsUserWithEmailRequestDTO;
import com.galsie.gcs.users.data.dto.registration.request.checks.CheckExistsUserWithPhoneNumberRequestDTO;
import com.galsie.gcs.users.data.dto.registration.request.checks.CheckExistsUserWithUsernameRequestDTO;
import com.galsie.gcs.users.repository.UserAccountRepository;
import com.galsie.gcs.users.repository.UserEmailRepository;
import com.galsie.gcs.users.repository.UserPhoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse.response;

@Service
public class UserExistenceService {
    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    UserEmailRepository userEmailRepository;

    @Autowired
    UserPhoneRepository userPhoneRepository;

    /**
     * Checks that a user with the username passed to {@link CheckExistsUserWithUsernameRequestDTO} exists
     * @param checkUserExistsDTO The DTO holding the username
     * @return GCSResponse with a Boolean being its entity
     */
    public GCSResponse<Boolean> existsUserWithUsername(CheckExistsUserWithUsernameRequestDTO checkUserExistsDTO){
        return response(gcsInternalExistsUserWithUsername(checkUserExistsDTO.getUsername()));
    }

    /**
     * gcsInternal methods
     * @param username
     * @return True if a user with the username exists, False otherwise
     */
    public boolean gcsInternalExistsUserWithUsername(String username){
        return userAccountRepository.findByUsername(username).isPresent();
    }


    /**
     * @param checkUserExistsDTO
     * @return GCSResponse with a Boolean being True if a user with the email exists, False otherwise
     */
    public GCSResponse<Boolean> existsUserWithEmail(CheckExistsUserWithEmailRequestDTO checkUserExistsDTO){
        return response(gcsInternalExistsUserWithEmail(checkUserExistsDTO.getEmail()));
    }
    /**
     * gcsInternal methods
     * @param email
     * @return True if a user with the email exists, False otherwise
     */
    public boolean gcsInternalExistsUserWithEmail(String email){
        return userEmailRepository.findByEmail(email).isPresent();
    }

    public GCSResponse<Boolean> existsUserWithPhone(CheckExistsUserWithPhoneNumberRequestDTO checkUserExistsDTO){
        return response(userPhoneRepository.findByCountryCodeAndPhoneNumber(checkUserExistsDTO.getCountryCode(), checkUserExistsDTO.getPhoneNumber()).isPresent());
    }

    /**
     * gcsInternal methods
     * @param countryCode
     * @param phoneNumber
     * @return True if a user with the email exists, False otherwise
     */
    public boolean gcsInternalExistsUserWithPhoneNumber(short countryCode, String phoneNumber){
        return userPhoneRepository.findByCountryCodeAndPhoneNumber(countryCode, phoneNumber).isPresent();
    }


}
