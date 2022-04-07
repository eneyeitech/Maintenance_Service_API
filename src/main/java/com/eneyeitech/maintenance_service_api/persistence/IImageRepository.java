package com.eneyeitech.maintenance_service_api.persistence;

import com.eneyeitech.maintenance_service_api.business.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IImageRepository extends CrudRepository<Image, Long> {
    Optional<Image> findByName(String name);
}
