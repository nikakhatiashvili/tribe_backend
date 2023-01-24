package com.example.student.groups.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table
public class TribeGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 20)
    private String tribeName;
    @NotNull
    @Size(min = 10, max = 100)
    private String tribeDescription;
    @NotNull
    @Size(min = 10, max = 100)
    private String adminId;

    public TribeGroup() {
    }

    public TribeGroup(Long id, String tribeName, String tribeDescription, String adminId) {
        this.id = id;
        this.tribeName = tribeName;
        this.tribeDescription = tribeDescription;
        this.adminId = adminId;
    }

    public TribeGroup(String tribeName, String tribeDescription, String adminId) {
        this.tribeName = tribeName;
        this.tribeDescription = tribeDescription;
        this.adminId = adminId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTribeName() {
        return tribeName;
    }

    public void setTribeName(String tribeName) {
        this.tribeName = tribeName;
    }

    public String getTribeDescription() {
        return tribeDescription;
    }

    public void setTribeDescription(String tribeDescription) {
        this.tribeDescription = tribeDescription;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }
}