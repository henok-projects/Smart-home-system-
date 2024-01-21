package com.galsie.gcs.users.data.dto.registration.request.register;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.gcs.users.data.common.UsernameHolder;
import com.galsie.lib.utils.crypto.hasher.HashingAlgorithm;
import com.sun.istack.NotNull;
import lombok.*;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
public class RegisterNewGalsieAccountRequestDTO implements UsernameHolder {
    @NotNull
    String username;

    @NotNull
    String otpVerificationToken;

    @NotNull
    String hashedPwd;


    public boolean isHashedPwdValid(){
        return HashingAlgorithm.SHA256.getEncodedLength(64) == hashedPwd.length();
    }
}
