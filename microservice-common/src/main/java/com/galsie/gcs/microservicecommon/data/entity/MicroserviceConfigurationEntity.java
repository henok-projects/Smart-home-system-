package com.galsie.gcs.microservicecommon.data.entity;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.*;

import javax.persistence.*;

/*

 */
@Entity
@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor

// config keys are unique to a microservice
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "microservice", "config_key" }) })

/*
Stores for a microservice, configuration. Configuration is shared between microservices of the same name-identifier
 */
public class MicroserviceConfigurationEntity implements GalEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /*
    Each microservice has a name identifier defined in application.yml
    A microservice might share a database with one of a different name identifier
    So the MicroserviceConfiguration tables holds the name identifier to dictate for which microservices the keys are.

    A microservice can have many configuration keys, and for each a value. Keys are unique for a microservice
     */
    @Column(name="microservice")
    private String microservice;

    @Column(name="config_key")
    private String configKey;

    @Column(name="config_value")
    private String configValue;

    public static String VERSION_KEY = "latest_version";
    public static String VERSION_REQUIRED_KEY = "latest_version_required";

}
