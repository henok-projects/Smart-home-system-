package com.galsie.gcs.users.editprofileinfo;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@SpringBootTest
public class EditUserProfileValidationTests {

    EditUserProfileAUX userProfileAUX = new EditUserProfileAUX();

//    @Test
    void contextLoads() {
    }

//    @Test
    void testValidEmailEditingVerificationToken(){
        var data = userProfileAUX.auxGetEmailRequestDto();
        data.getEditingVerificationToken();
        assertEquals(data.isValidEditingVerificationToken(), true);
    }

//    @Test
    void testInvalidEmailEditingVerificationToken(){
        var data = userProfileAUX.auxGetInvalidEmailRequestDto();
        data.getEditingVerificationToken();
        assertEquals(data.isValidEditingVerificationToken(), true);
    }

//    @Test
    void testValidNewEmailVerificationToken(){
        var data = userProfileAUX.auxGetEmailRequestDto();
        data.getNewEmailVerificationToken();
        assertEquals(data.isValidEditingVerificationToken(), true);

    }

//    @Test
    void testInvalidNewEmailVerificationToken(){
        var data = userProfileAUX.auxGetInvalidEmailRequestDto();
        data.getNewEmailVerificationToken();
        assertEquals(data.isValidEditingVerificationToken(), true);

    }

//    @Test
    void testValidPhoneEditingVerificationToken(){
        var data = userProfileAUX.auxGetPhoneRequestDto();
        data.getEditingVerificationToken();
        assertEquals(data.isValidEditingVerificationToken(), true);

    }

//    @Test
    void testInvalidPhoneEditingVerificationToken(){
        var data = userProfileAUX.auxGetInvalidPhoneRequestDto();
        assertEquals(data.isValidEditingVerificationToken(), true);
        assertEquals(true, data.getEditingVerificationToken().length()==1);

    }

//    @Test
    void testValidNewPhoneVerificationToken(){
        var data = userProfileAUX.auxGetPhoneRequestDto();
        data.getNewPhoneVerificationToken();
        assertEquals(data.isValidEditingVerificationToken(), true);
    }

//    @Test
    void testInvalidNewPhoneVerificationToken(){
        var data = userProfileAUX.auxGetInvalidPhoneRequestDto();
        data.getNewPhoneVerificationToken();
        assertEquals(data.isValidEditingVerificationToken(), true);
    }

//    @Test
    void testValidUsernameEditingVerificationToken(){
        var data = userProfileAUX.auxGetUsernameRequestDto();
        data.getEditingVerificationToken();
        assertEquals(data.isValidEditingVerificationToken(), true);
    }

//    @Test
    void testInvalidUsernameEditingVerificationToken(){
        var data = userProfileAUX.auxGetInvalidUsernameRequestDto();
        data.getEditingVerificationToken();
        assertEquals(data.isValidEditingVerificationToken(), true);
    }

//    @Test
    void testInvalidUsername(){
        var data = userProfileAUX.auxGetInvalidUsernameRequestDto();
        data.getUsername();
        assertEquals(data.isUsernameValid(), false);
    }
//    @Test
    void testValidFullnameEditingVerificationToken(){
        var data = userProfileAUX.auxGetFullNameRequestDto();
        data.getEditingVerificationToken();
        assertEquals(data.isValidEditingVerificationToken(), true);

    }

//    @Test
    void testValidFullNameEditingVerificationToken(){
        var data = userProfileAUX.auxGetFullNameRequestDto();
        assertEquals(data.isValidEditingVerificationToken(), true);

    }
//    @Test
    void testValidFullName(){
        var data = userProfileAUX.auxGetFullNameRequestDto();
        assertEquals(data.isValidFirstName(), true);
        assertEquals(data.isValidLastName(), true);
        assertEquals(data.isValidMiddleName(), true);
    }

//    @Test
    void testInvalidFullNameEditingVerificationToken(){
        var data = userProfileAUX.auxGetInvalidFullNameRequestDto();
        assertEquals(data.getFirstName().length()==1, true);
        assertEquals(data.getLastName().length()==1, true);
        assertEquals(data.getMiddleName().length()==1, true);    }

//    @Test
    void testValidGenderEditingVerificationToken(){
        var data = userProfileAUX.auxGetGenderRequestDto();
        data.getEditingVerificationToken();
        assertEquals(data.isValidEditingVerificationToken(), true);

    }

//    @Test
    void testInvalidGenderEditingVerificationToken(){
        var data = userProfileAUX.auxGetInvalidGenderRequestDto();
        data.getEditingVerificationToken();
        assertEquals(data.isValidEditingVerificationToken(), true);
    }

//    @Test
    void testInvalidGender(){
        var data = userProfileAUX.auxGetInvalidGenderRequestDto();
        data.getGender();
        assertEquals(data.isValidGender(), false);
    }


//    @Test
    void testValidPassw0rdEditingVerificationToken(){
        var data = userProfileAUX.auxGetPasswordRequestDto();
        data.getEditingVerificationToken();
        assertEquals(data.isValidEditingVerificationToken(), true);

    }

//    @Test
    void testInvalidPasswordEditingVerificationToken(){
        var data = userProfileAUX.auxGetInvalidPasswordRequestDto();
        data.getEditingVerificationToken();
        assertEquals(data.isValidEditingVerificationToken(), true);
    }

//    @Test
    void testInvalidPassword(){
        var data = userProfileAUX.auxGetInvalidPasswordRequestDto();
        data.getHashedPwd();
        assertEquals(data.isHashedPwdValid(), false);
    }

//    @Test
    void testValidBirthDateEditingVerificationToken(){
        var data = userProfileAUX.auxGetBirthdateRequestDto();
        data.getEditingVerificationToken();
        assertEquals(data.isValidEditingVerificationToken(), true);

    }

//    @Test
    void testInvalidBirthDateEditingVerificationToken(){
        var data = userProfileAUX.auxGetInvalidBirthdateRequestDto();
        data.getEditingVerificationToken();
        assertEquals(data.isValidEditingVerificationToken(), true);
    }

//    @Test
    void testInvalidBirthDate(){
        var data = userProfileAUX.auxGetInvalidBirthdateRequestDto();
        data.getBirthDate();
        assertEquals(data.isValidBirthdate(), false);
    }

}
