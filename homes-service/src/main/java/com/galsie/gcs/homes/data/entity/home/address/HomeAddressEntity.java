package com.galsie.gcs.homes.data.entity.home.address;
import com.galsie.gcs.homes.data.entity.home.HomeEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.*;
import reactor.util.annotation.Nullable;
import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HomeAddressEntity implements GalEntity<Long> {
    @Id
    @Column(name = "home_id", nullable = false)
    Long id;

    @OneToOne
    @MapsId
    HomeEntity home;

    @Column(name="country_id", nullable = false)
    long countryId;

    @Column(name="zone_id", nullable = false)
    long zoneId;

    @Column(name="city_id", nullable = false)
    long cityId;

    @Column(name="post_code", nullable = false)
    String postCode;

    @Column(name = "address_Line1", nullable = false)
    String addressLine1;

    @Column(name = "address_Line2")
    String addressLine2;

    @Nullable
    @Column(name = "longitude")
    Double longitude;

    @Column(name = "latitude")
    Double latitude;


}
