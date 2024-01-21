package com.galsie.gcs.users.usersecuritypin;


import com.galsie.gcs.users.data.dto.editprofileinfo.pin.request.SetupUserSecurityAppPinRequestDTO;
import java.util.Random;

public class UserSecurityPinSetupPinAux {

    Random random = new Random();

    private String auxGetRandomData(){
        return String.valueOf((int)(Math.random()*100000));
    }

    private String auxGetInvalidRandomData(){
        return String.valueOf((int)(Math.random()*0));
    }

    public SetupUserSecurityAppPinRequestDTO auxGetSetupPinRequest() {
        return new SetupUserSecurityAppPinRequestDTO(auxGetRandomData(), auxGetRandomData());
    }

    public SetupUserSecurityAppPinRequestDTO auxGetInvalidSetupPinRequest() {
        return  new SetupUserSecurityAppPinRequestDTO(auxGetInvalidRandomData(), auxGetRandomData());
    }

}


