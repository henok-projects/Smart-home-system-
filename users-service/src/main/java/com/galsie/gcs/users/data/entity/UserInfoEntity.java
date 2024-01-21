package com.galsie.gcs.users.data.entity;


import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import com.galsie.gcs.users.data.discrete.UserGender;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Builder
@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class UserInfoEntity implements GalEntity<Long> {

    @Id
    @Column(name = "user_id") // foreign key would be the same as the user id. The column name doesn't matter
    private Long id;

    @OneToOne(cascade=CascadeType.ALL)
    @MapsId // the @MapsId annotation, which indicates that the primary key values will be copied from the User entity.
    @JoinColumn(name="user_id")
    private UserAccountEntity user;

    @Column(name="first_name", nullable = true)
    String firstName;


    @Column(name="last_name", nullable = true)
    String lastName;


    @Column(name="middle_name", nullable = true)
    String middleName;


    @Column(name="family", nullable = true)
    String family;

    @Column(name="birth_date", nullable = true)
    LocalDate birthDate;

    @Column(name="gender", nullable = true)
    UserGender gender;

    public String getName() {
        return firstName + " "+ middleName +" " + lastName;
    }

}
