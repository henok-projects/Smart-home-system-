package com.galsie.gcs.homes.data.entity.home.user;


import com.galsie.gcs.homes.data.discrete.HomeUserStatus;
import com.galsie.gcs.homes.data.entity.home.HomeEntity;
import com.galsie.gcs.homes.data.entity.home.invites.EmailOrPhoneBasedHomeInviteEntity;
import com.galsie.gcs.homes.data.entity.home.rolesandaccess.HomeRoleEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HomeUserAccessInfoEntity implements GalEntity<Long> {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "home_user_entity_id")
    @MapsId
    private HomeUserEntity homeUserEntity;

    @OneToOne
    private HomeEntity homeEntity;

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    HomeUserStatus homeUserStatus; // TODO: Be sure status is accurately set everywhere

    @Column(name = "is_deleted_from_home")
    boolean isDeletedFromHome; // TODO: Be sure status is accurately set when user is deleted

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.DETACH}, mappedBy = "accessInfo")
    List<HomeRoleEntity> roles;

    @OneToMany
    private List<EmailOrPhoneBasedHomeInviteEntity> inviteEntities;

    @Column(name="with_permissions")
    @ElementCollection
    List<String> withPermissions;

    @Column(name="without_permissions")
    @ElementCollection
    List<String> withoutPermissions;

    @Column(name="access_start_date")
    LocalDateTime accessStartDate;

    @Column(name="access_end_date")
    LocalDateTime accessEndDate;

}
