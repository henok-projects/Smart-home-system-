package com.galsie.gcs.users.data.entity.security.usersecuritypin;


import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.*;

import javax.persistence.*;

@Builder
@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class UserPinSecurityEntity implements GalEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name="hashed_pin", nullable = false)
    String hashedPin;

}


