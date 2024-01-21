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
public class HomeRoleSubCategoryPermissionEntity implements GalEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_permission_id", nullable = false)
    private HomeRoleCategoryPermissionEntity categoryPermissionEntity;

    @Column(name = "sub_category_key")
    private String subCategoryKey;

    @Column(name = "sub_category_name")
    private String subCategoryName;

    @ElementCollection(fetch = FetchType.LAZY)
    @Column(name = "permission")
    private List<String> withPermissions;

    @ElementCollection(fetch = FetchType.LAZY)
    @Column(name = "permission")
    private List<String> withoutPermissions;
}
