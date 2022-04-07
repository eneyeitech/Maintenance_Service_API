package com.eneyeitech.maintenance_service_api.persistence;

import com.eneyeitech.maintenance_service_api.business.Building;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBuildingRepository extends CrudRepository<Building, String> {
}
