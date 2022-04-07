package com.eneyeitech.maintenance_service_api.presentation.api;

import com.eneyeitech.maintenance_service_api.business.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class BusinessController {

    @Autowired
    BuildingService buildingService;

    @Autowired
    UserService userService;

    @PutMapping("api/tenant/{email}/building/{bid}")
    public Object assignTenantToBuilding(@AuthenticationPrincipal UserDetailsImpl details, @PathVariable String email, @PathVariable String bid){
        if (details == null) {
            return new ResponseEntity<>(Map.of("error", "email not valid"), HttpStatus.BAD_REQUEST);
        }

        User tenant = userService.findUserByEmail(email);
        Building apartment = buildingService.findBuildingById(bid);
        if (tenant == null) {
            return new ResponseEntity<>(Map.of("error", "tenant not found"), HttpStatus.NOT_FOUND);
        }
        if (apartment == null) {
            return new ResponseEntity<>(Map.of("error", "apartment not found"), HttpStatus.NOT_FOUND);
        }
        User user = details.getUser();
        if(!user.getCompany().getId().equalsIgnoreCase(apartment.getCompany().getId())){
            return new ResponseEntity<>(Map.of("error", "user not authorised to assign tenant"), HttpStatus.UNAUTHORIZED);
        }

        apartment.setUser(tenant);

        return buildingService.update(apartment);
    }



}
