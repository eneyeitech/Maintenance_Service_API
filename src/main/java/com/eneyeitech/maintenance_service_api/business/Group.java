package com.eneyeitech.maintenance_service_api.business;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "principle_groups")
public class Group{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String code;
    private String name;

    public Group() {
    }

    public Group(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Group{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @ManyToMany(mappedBy = "userGroups", fetch = FetchType.EAGER )
    private Set<User> users;
}

