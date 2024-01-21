package com.galsie.gcs.serviceutilslibrary.utils.gcs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface GalRepository<T, ID> extends JpaRepository<T, ID> {


}
