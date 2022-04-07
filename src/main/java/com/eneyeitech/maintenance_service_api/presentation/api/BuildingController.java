package com.eneyeitech.maintenance_service_api.presentation.api;

import com.eneyeitech.maintenance_service_api.business.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class BuildingController {

    @Autowired
    BuildingService buildingService;

    @Autowired
    MaintenanceService maintenanceService;

    @PostMapping("api/building/new")
    public Object addBuilding(@AuthenticationPrincipal UserDetailsImpl details, @RequestBody Building building){
        if (details == null) {
            return new ResponseEntity<>(Map.of("error", "email not valid"), HttpStatus.BAD_REQUEST);
        }
        User user = details.getUser();
        Company company = user.getCompany();
        building.setCompany(company);
        return  buildingService.createBuilding(building);
    }

    @PostMapping("api/building/{id}/maintenance")
    public Object requestMaintenance(@AuthenticationPrincipal UserDetailsImpl details, @RequestBody Maintenance maintenance, @PathVariable String id){
        if (details == null) {
            return new ResponseEntity<>(Map.of("error", "email not valid"), HttpStatus.BAD_REQUEST);
        }
        User user = details.getUser();

        Building buildingToMaintain = buildingService.findBuildingById(id);
        if(user.getId() != buildingToMaintain.getUser().getId()){
            return new ResponseEntity<>(Map.of("error", "tenant cannot make request"), HttpStatus.FORBIDDEN);
        }

        if(buildingToMaintain == null){
            return new ResponseEntity<>(Map.of("error", "building not found"), HttpStatus.NOT_FOUND);
        }
        maintenance.setBuilding(buildingToMaintain);
        return maintenanceService.createMaintenance(maintenance);
    }
}
