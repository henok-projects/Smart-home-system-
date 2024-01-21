package com.galsie.gcs.resources.data.entity.roleestablishement;

import com.galsie.gcs.resources.data.discrete.GCSResourceMicroserviceRole;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResourcesMicroserviceInstanceRoleEntity implements GalEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="unique_id")
    private Long id;

    private String uniqueNameInstanceId;

    private GCSResourceMicroserviceRole role;

}
