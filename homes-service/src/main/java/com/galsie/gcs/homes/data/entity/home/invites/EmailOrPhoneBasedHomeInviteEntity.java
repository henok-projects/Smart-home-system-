package com.galsie.gcs.homes.data.entity.home.invites;


import com.galsie.gcs.homes.data.constant.HomeUserInviteType;
import com.galsie.gcs.homes.data.entity.home.HomeEntity;
import com.galsie.gcs.homes.data.entity.home.common.PhoneNumberEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.*;
import org.springframework.lang.Nullable;
import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@DiscriminatorValue(HomeUserInviteType.EMAIL_OR_PHONE_BASED)
public class EmailOrPhoneBasedHomeInviteEntity extends HomeDirectUserInviteEntity implements GalEntity<Long> {

    @Nullable
    @Column(name = "full_name")
    String fullName;

    @Nullable
    @Column(name = "email")
    private String email;

    @OneToOne
    private PhoneNumberEntity phoneNumber;

    @Builder
    public EmailOrPhoneBasedHomeInviteEntity(Long id, HomeEntity homeEntity, HomeInviteAccessInfoEntity accessInfo, String inviteUniqueCode, @Nullable String fullName, @Nullable String email, PhoneNumberEntity phoneNumber) {
        super();
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}