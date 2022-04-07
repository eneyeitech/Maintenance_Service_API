package com.eneyeitech.maintenance_service_api.business;

import com.eneyeitech.maintenance_service_api.persistence.IBuildingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BuildingService {
    private final IBuildingRepository buildingRepository;

    @Autowired
    public BuildingService(
            IBuildingRepository buildingRepository
    ){
        this.buildingRepository = buildingRepository;
    }

    public Building createBuilding(Building buildingToCreate){
        return buildingRepository.save(buildingToCreate);
    }

    public Building update(Building buildingToUpdate){
        return buildingRepository.save(buildingToUpdate);
    }

    public Building findBuildingById(String id){
        Optional<Building> building = buildingRepository.findById(id);
        Building foundBuilding = building.orElse(null);
        return foundBuilding;
    }

    public boolean doesBuildingExist(String id){
        return findBuildingById(id) != null ? true:false;
    }
}
