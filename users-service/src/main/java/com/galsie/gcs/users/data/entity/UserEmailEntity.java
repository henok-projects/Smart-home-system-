package com.galsie.gcs.users.data.entity;


import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class UserEmailEntity implements GalEntity<Long> {
    @Id
    @Column(name = "user_id") // foreign key would be the same as the user id. The column name doesn't matter
    private Long id;

    @OneToOne(cascade=CascadeType.ALL)
    @MapsId // the @MapsId annotation, which indicates that the primary key values will be copied from the User entity.
    @JoinColumn(name="user_id")
    private UserAccountEntity user;

    @Column(name="email")
    private String email;


    @Column(name="created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name="modified_at")
    private  LocalDateTime modifiedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        modifiedAt = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        modifiedAt = LocalDateTime.now();
    }

//    @Override
//    public UserEmailDTO toDTO() {
//        return new UserEmailDTO(getId(), user.getId(), email, createdAt, modifiedAt);
//    }
//
//    public boolean isVerified(){
//        return this.verifiedAt != null;
//    }
}
