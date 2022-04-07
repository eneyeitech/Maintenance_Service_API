package com.eneyeitech.maintenance_service_api.business;

import com.eneyeitech.maintenance_service_api.persistence.IBuildingRepository;
import com.eneyeitech.maintenance_service_api.persistence.IMaintenanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaintenanceService {
    private final IMaintenanceRepository maintenanceRepository;

    @Autowired
    public MaintenanceService(
            IMaintenanceRepository maintenanceRepository
    ){
        this.maintenanceRepository = maintenanceRepository;
    }

    public Maintenance createMaintenance(Maintenance maintenanceToCreate){
        return maintenanceRepository.save(maintenanceToCreate);
    }
}
