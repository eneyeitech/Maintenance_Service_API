package com.eneyeitech.maintenance_service_api.business;

import com.eneyeitech.maintenance_service_api.utility.Helper;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Building {
    @Id
    @Column(name = "id", nullable = false)
    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String id = Helper.getNewUUID();
    private String name;
    private String state;
    private String address;
    @Column(nullable = true, updatable = true, name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    @LastModifiedDate
    private Date date;

    @Transient
    private List<String> maintenanceList = new ArrayList<>();

    @OneToMany(mappedBy = "building")
    private List<Maintenance> maintenances = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
