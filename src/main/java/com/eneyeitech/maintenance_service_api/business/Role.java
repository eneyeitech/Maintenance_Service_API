package com.eneyeitech.maintenance_service_api.business;

import javax.validation.constraints.NotEmpty;

public class Role {
    @NotEmpty
    private String user;
    private String role;
    private String operation;

    public Role() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
