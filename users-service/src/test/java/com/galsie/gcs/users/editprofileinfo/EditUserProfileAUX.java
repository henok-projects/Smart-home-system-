package com.galsie.gcs.users.editprofileinfo;

import com.galsie.gcs.users.data.dto.editprofileinfo.dateofbirth.request.EditUserBirthdateRequestDTO;
import com.galsie.gcs.users.data.dto.editprofileinfo.email.request.EditUserEmailRequestDTO;
import com.galsie.gcs.users.data.dto.editprofileinfo.fullname.request.EditUserFullNameRequestDTO;
import com.galsie.gcs.users.data.dto.editprofileinfo.gender.request.EditUserGenderRequestDTO;
import com.galsie.gcs.users.data.dto.editprofileinfo.password.request.EditUserPasswordRequestDTO;
import com.galsie.gcs.users.data.dto.editprofileinfo.phone.request.EditUserPhoneRequestDTO;
import com.galsie.gcs.users.data.dto.editprofileinfo.username.request.EditUserUsernameRequestDTO;
import org.junit.jupiter.api.Test;

public class EditUserProfileAUX {

    @Test
    void contextLoads() {
    }

    private String auxGetRandomData(){
        return String.valueOf((int)(Math.random()*100000));
    }

    private String auxGetInvalidRandomData(){
        return String.valueOf((int)(Math.random()*0));
    }

    public EditUserEmailRequestDTO auxGetEmailRequestDto(){
        return new EditUserEmailRequestDTO(auxGetRandomData(), auxGetRandomData());
    }

    public EditUserEmailRequestDTO auxGetInvalidEmailRequestDto(){
        return new EditUserEmailRequestDTO(auxGetInvalidRandomData(), auxGetInvalidRandomData());
    }

    public EditUserPhoneRequestDTO auxGetPhoneRequestDto(){
        return new EditUserPhoneRequestDTO(auxGetRandomData(), auxGetRandomData());
    }

    public EditUserPhoneRequestDTO auxGetInvalidPhoneRequestDto(){
        return new EditUserPhoneRequestDTO(auxGetInvalidRandomData(), auxGetInvalidRandomData());
    }

    public EditUserUsernameRequestDTO auxGetUsernameRequestDto(){
        return new EditUserUsernameRequestDTO(auxGetRandomData(), auxGetRandomData());
    }

    public EditUserUsernameRequestDTO auxGetInvalidUsernameRequestDto(){
        return new EditUserUsernameRequestDTO(auxGetInvalidRandomData(), auxGetInvalidRandomData());
    }
    public EditUserGenderRequestDTO auxGetGenderRequestDto(){
        return new EditUserGenderRequestDTO(auxGetRandomData(), auxGetRandomData());
    }

    public EditUserGenderRequestDTO auxGetInvalidGenderRequestDto(){
        return new EditUserGenderRequestDTO(auxGetInvalidRandomData(), auxGetInvalidRandomData());
    }

    public EditUserFullNameRequestDTO auxGetFullNameRequestDto(){
        return new EditUserFullNameRequestDTO(auxGetRandomData(), auxGetRandomData(),auxGetRandomData(),auxGetRandomData());
    }

    public EditUserFullNameRequestDTO auxGetInvalidFullNameRequestDto(){
        return new EditUserFullNameRequestDTO(auxGetInvalidRandomData(), auxGetInvalidRandomData(),auxGetInvalidRandomData(),auxGetInvalidRandomData());
    }

    public EditUserBirthdateRequestDTO auxGetBirthdateRequestDto(){
        return new EditUserBirthdateRequestDTO(auxGetRandomData(), auxGetRandomData());
    }

    public EditUserBirthdateRequestDTO auxGetInvalidBirthdateRequestDto(){
        return new EditUserBirthdateRequestDTO(auxGetInvalidRandomData(), auxGetInvalidRandomData());
    }

    public EditUserPasswordRequestDTO auxGetPasswordRequestDto(){
        return new EditUserPasswordRequestDTO(auxGetRandomData(), auxGetRandomData());
    }

    public EditUserPasswordRequestDTO auxGetInvalidPasswordRequestDto(){
        return new EditUserPasswordRequestDTO(auxGetInvalidRandomData(), auxGetInvalidRandomData());
    }

}

