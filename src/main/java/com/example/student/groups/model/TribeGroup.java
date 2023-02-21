package com.example.student.groups.model;

import com.example.student.student.TribeUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table
public class TribeGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 20)
    private String tribeName;

    @ElementCollection
    private Set<Long> members = new HashSet<>();
    @NotNull
    @Size(min = 10, max = 100)
    private String tribeDescription;
    @NotNull
    @Size(min = 10, max = 100)
    private String adminId;

    public TribeGroup() {
    }

    public void addMember(TribeUser user) {
        members.add(user.getId());
        user.getGroups().add(this.getId());
    }

    public void removeMember(TribeUser user) {
        members.remove(user.getId());
        user.getGroups().remove(this.getId());
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

    public Set<Long> getMembers() {
        return members;
    }

    public void setMembers(Set<Long> members) {
        this.members = members;
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