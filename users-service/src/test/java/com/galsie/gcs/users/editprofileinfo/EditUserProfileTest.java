package com.galsie.gcs.users.editprofileinfo;

import com.galsie.gcs.users.service.editprofileinfo.EditUserProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
public class EditUserProfileTest {

//    @Autowired
    EditUserProfileService editUserProfileService;

    EditUserProfileAUX editUserProfileAUX = new EditUserProfileAUX();

//    @Test
    void contextLoads() {
    }

//    @Test
    void testEditUserProfileByUsername(){
        var dto = editUserProfileAUX.auxGetUsernameRequestDto();
        var response = editUserProfileService.requestEditUsername(dto);
        assert !response.hasError();
    }

//    @Test
    void testEditUserProfileByEmail(){
        var dto = editUserProfileAUX.auxGetEmailRequestDto();
        var response = editUserProfileService.requestEditEmail(dto);
        assert !response.hasError();
    }

//    @Test
    void testEditUserProfileByPhone(){
        var dto = editUserProfileAUX.auxGetPhoneRequestDto();
        var response = editUserProfileService.requestEditPhone(dto);
        assert !response.hasError();
    }

//    @Test
    void testEditUserProfileByGender(){
        var dto = editUserProfileAUX.auxGetGenderRequestDto();
        var response = editUserProfileService.requestEditGender(dto);
        assert !response.hasError();
    }

//    @Test
    void testEditUserProfileByFullname(){
        var dto = editUserProfileAUX.auxGetFullNameRequestDto();
        var response = editUserProfileService.requestEditFullName(dto);
        assert !response.hasError();
    }

//    @Test
    void testEditUserProfileByPasswOrd(){
        var dto = editUserProfileAUX.auxGetPasswordRequestDto();
        var response = editUserProfileService.requestEditPassword(dto);
        assert !response.hasError();
    }

//    @Test
    void testEditUserProfileByBirthdate(){
        var dto = editUserProfileAUX.auxGetBirthdateRequestDto();
        var response = editUserProfileService.requestEditBirthdate(dto);
        assert !response.hasError();
    }

}

