package com.example.student.student;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table
public class TribeUser {

    @Id
    @SequenceGenerator(name = "student_sequence", sequenceName = "student_sequence", allocationSize = 1)
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "student_sequence"
    )
    private long id;

    @NotNull
    @Size(min = 10, max = 100)
    private String firebaseId;
    private boolean hasCreatedGroup;
    @ElementCollection
    @CollectionTable(name = "user_group", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "group_id")
    private Set<Long> groups = new HashSet<>();

    @NotNull
    @Size(min = 3, max = 40)
    private String name;

    @NotEmpty
    @NotNull
    private String timezone;

    @NotNull
    @NotEmpty(message = "email is required")
    @Email(message = "Invalid email address")
    private String email;

    public TribeUser(long id, String name, String email, String firebaseId,Boolean hasCreatedGroup) {
        this.firebaseId = firebaseId;
        this.id = id;
        this.name = name;
        this.email = email;
        this.hasCreatedGroup = hasCreatedGroup;
    }

    public TribeUser(String name, String email, String firebaseId) {
        this.firebaseId = firebaseId;
        this.name = name;
        this.email = email;
    }

    public TribeUser() {
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public void addGroup(Long groupId) {
        groups.add(groupId);
    }

    public void removeGroup(Long groupId) {
        groups.remove(groupId);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firebase_id='" + firebaseId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
    public Set<Long> getGroups() {
        return groups;
    }

    public void setGroups(Set<Long> groups) {
        this.groups = groups;
    }
    public boolean isHasCreatedGroup() {
        return hasCreatedGroup;
    }

    public void setHasCreatedGroup(boolean hasCreatedGroup) {
        this.hasCreatedGroup = hasCreatedGroup;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }
}
