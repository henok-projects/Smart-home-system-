package com.galsie.gcs.homes.data.entity.home.invites;

import com.galsie.gcs.homes.data.entity.home.rolesandaccess.HomeRoleEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.lang.Nullable;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HomeInviteAccessInfoEntity implements GalEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invite_access_info_id", nullable = false)
    Long id;

    @OneToMany(cascade = CascadeType.MERGE, orphanRemoval = true, mappedBy = "accessInfo")
    List<HomeRoleEntity> roles;

    @Column(name = "access_start_date", nullable = false)
    @NotNull
    LocalDateTime accessStartDate;

    @Column(name = "access_expiry", nullable = false)
    @Nullable
    LocalDateTime accessEndDate; // when does the user access expire after the invite has been accepted

    @ElementCollection
    @Column(name="with_permissions")
    List<String> withPermissions;

    @ElementCollection
    @Column(name="without_permissions")
    List<String> withoutPermissions;

}
