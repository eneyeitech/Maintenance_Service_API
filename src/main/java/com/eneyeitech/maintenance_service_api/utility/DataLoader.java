package com.eneyeitech.maintenance_service_api.utility;

import com.eneyeitech.maintenance_service_api.business.Group;
import com.eneyeitech.maintenance_service_api.business.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    private GroupService groupService;

    @Autowired
    public DataLoader(GroupService groupService) {
        this.groupService = groupService;
        createRoles();
    }

    private void createRoles() {
        try {
            groupService.save(new Group("ROLE_MANAGER", "Manager Group"));
            groupService.save(new Group("ROLE_USER", "User Group"));
            groupService.save(new Group("ROLE_ADMINISTRATOR", "Administrator Group"));
            groupService.save(new Group("ROLE_TENANT", "Tenant Group"));
            groupService.save(new Group("ROLE_TECHNICIAN", "Technician Group"));
        } catch (Exception e) {

        }
    }
}
