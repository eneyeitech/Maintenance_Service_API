package com.eneyeitech.maintenance_service_api.persistence;

import com.eneyeitech.maintenance_service_api.business.Maintenance;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMaintenanceRepository extends CrudRepository<Maintenance, String> {
}
