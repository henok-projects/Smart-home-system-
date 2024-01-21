package com.galsie.gcs.resources.data.entity.countries;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "phone_code")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PhoneCodeEntity implements GalEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="phone_code_id", nullable = false)
    private Long id;

    @Column(name ="phone_code")
    private int code;

    @ManyToOne
    private CountryEntity country;
}
