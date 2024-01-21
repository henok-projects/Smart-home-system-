package com.galsie.gcs.homes.data.dto.invites.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.discrete.homeinvite.HomeQRUserInviteResponseErrorType;
import com.galsie.gcs.homes.data.dto.common.PhoneNumberDTO;
import com.galsie.gcs.homes.data.dto.common.UserHomeAccessInfoDTO;
import com.galsie.gcs.homes.data.entity.home.HomeEntity;
import com.galsie.gcs.homes.data.entity.home.invites.EmailOrPhoneBasedHomeInviteEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import lombok.*;
import org.springframework.lang.Nullable;
import java.util.Optional;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HomeDirectUserInviteByEmailOrPhoneDTO extends HomeDirectUserInviteByCommon {

    @JsonProperty("email")
    @Nullable
    private String email;

    @JsonProperty("phone_number")
    @Nullable
    private PhoneNumberDTO phone;

    @JsonProperty("full_name")
    @Nullable
    private String fullName;

    public boolean isEmailOrPhoneNotNull(){
        return phone != null || email != null;
    }

    public boolean isValidEmail(){
        return this.getEmail().matches("^([a-zA-Z0-9_\\-.]+)@([a-zA-Z0-9_\\-]+)(\\.[a-zA-Z]{1,64}){1,2}$");
    }

    public Optional<HomeQRUserInviteResponseErrorType> isEmailOrPhoneDTOValid(){
        if(!this.isValidEmail()){
            return Optional.of(HomeQRUserInviteResponseErrorType.USER_NOT_FOUND);
        }

        if(!this.isEmailOrPhoneNotNull()){
            return Optional.of(HomeQRUserInviteResponseErrorType.USER_NOT_FOUND);
        }

        return Optional.empty();
    }

    public EmailOrPhoneBasedHomeInviteEntity toInviteUserByEmailOrPhoneEntity(HomeEntity homeEntity, UserHomeAccessInfoDTO accessInfoDTO) {

            var accessInfo = accessInfoDTO != null
                    ? UserHomeAccessInfoDTO.toInviteAccessInfoEntity(accessInfoDTO)
                    : null;
            var phoneNumberEntity = phone != null
                    ? PhoneNumberDTO.toPhoneNumberEntity(this.phone)
                    : null;

            return EmailOrPhoneBasedHomeInviteEntity.builder()
                    .email(email)
                    .fullName(fullName)
                    .homeEntity(homeEntity)
                    .accessInfo(accessInfo)
                    .phoneNumber(phoneNumberEntity)
                    .build();
        }
}

