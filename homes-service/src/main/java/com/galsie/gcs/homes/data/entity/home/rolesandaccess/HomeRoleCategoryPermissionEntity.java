package com.galsie.gcs.homes.data.entity.home.rolesandaccess;

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
public class HomeRoleCategoryPermissionEntity implements GalEntity<Long> {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "category_key")
        private String categoryKey;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "home_role_id", nullable = false)
        private HomeRoleEntity homeRole;

        @OneToMany(mappedBy = "categoryPermissionEntity", cascade = CascadeType.ALL)
        private List<HomeRoleSubCategoryPermissionEntity> subCategoryPermissionEntities;



}
