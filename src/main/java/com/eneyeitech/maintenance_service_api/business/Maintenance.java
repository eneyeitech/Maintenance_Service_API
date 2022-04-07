package com.eneyeitech.maintenance_service_api.business;

import com.eneyeitech.maintenance_service_api.utility.Helper;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Maintenance {
    @Id
    @Column(name = "id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String id = Helper.getNewUUID();
    private String name;
    private String description;
    private String status="Pending";
    @Column(nullable = true, updatable = true, name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    @LastModifiedDate
    private Date date;

    @ManyToOne
    @JoinColumn(name = "building_id")
    private Building building;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @PrePersist
    private void onCreate() {
        date = new Date();
    }

    @PreUpdate
    private void onUpdate() {
        date = new Date();
    }
}
