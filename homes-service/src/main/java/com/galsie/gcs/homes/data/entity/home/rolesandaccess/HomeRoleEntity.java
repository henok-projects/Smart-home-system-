package com.galsie.gcs.homes.data.entity.home.rolesandaccess;

import com.galsie.gcs.homes.data.entity.home.HomeEntity;
import com.galsie.gcs.homes.data.entity.home.user.HomeUserAccessInfoEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HomeRoleEntity implements GalEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_id")
    HomeEntity home;

    @ManyToMany
    List<HomeUserAccessInfoEntity> accessInfo;

    @OneToMany(mappedBy = "homeRole", cascade = CascadeType.ALL)
    private List<HomeRoleCategoryPermissionEntity> categories;

    @Column(name = "name")
    private String name;

    @Column(name = "base64_encoded_image")
    private String base64EncodedImage;

}
