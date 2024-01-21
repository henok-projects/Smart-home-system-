package com.galsie.gcs.homes.data.entity.home.common;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.*;
import org.springframework.lang.Nullable;
import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PhoneNumberEntity implements GalEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name="country_code")
    @Nullable
    private String countryCode;

    @Column(name="phone")
    @Nullable
    private String phone;

}
