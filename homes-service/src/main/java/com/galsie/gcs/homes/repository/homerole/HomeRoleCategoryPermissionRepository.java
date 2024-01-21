package com.galsie.gcs.homes.repository.homerole;

import com.galsie.gcs.homes.data.entity.home.rolesandaccess.HomeRoleCategoryPermissionEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeRoleCategoryPermissionRepository extends GalRepository<HomeRoleCategoryPermissionEntity, Long> {
}
