package com.eneyeitech.maintenance_service_api.business;

import com.eneyeitech.maintenance_service_api.utility.Helper;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Company {
    @Id
    @Column(name = "id", nullable = false)
    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String id = Helper.getNewUUID();
    private String name;
    private String town;
    private String state;
    private String street;
    private String number;
    private String officePhoneNumber;
    private String cacNumber;
    @Column(nullable = true, updatable = true, name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    @LastModifiedDate
    private Date date;

    @Transient
    private List<String> userList = new ArrayList<>();

    @Transient
    private List<String> buildingList = new ArrayList<>();

    @OneToMany(mappedBy = "company")
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "company")
    private List<Building> buildings = new ArrayList<>();

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

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getOfficePhoneNumber() {
        return officePhoneNumber;
    }

    public void setOfficePhoneNumber(String officePhoneNumber) {
        this.officePhoneNumber = officePhoneNumber;
    }

    public String getCacNumber() {
        return cacNumber;
    }

    public void setCacNumber(String cacNumber) {
        this.cacNumber = cacNumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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
