package com.galsie.gcs.homes.repository.homerole;

import com.galsie.gcs.homes.data.entity.home.rolesandaccess.HomeRoleEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeRoleEntityRepository extends GalRepository<HomeRoleEntity, Long> {
    @Query("SELECT hre FROM HomeRoleEntity hre JOIN hre.home h WHERE h.id = :homeId AND EXISTS (SELECT 1 FROM AbstractHomeUserEntity aue WHERE aue.home.id = :homeId AND aue.appUser.id = :userId)")
    List<HomeRoleEntity> findByUserIdAndHomeId(@Param("userId") Long userId, @Param("homeId") Long homeId);

}
