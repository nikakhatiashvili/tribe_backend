package com.example.student.user;

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

    private boolean hasCreatedGroup;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Long> groups = new HashSet<>();

    @NotNull
    @Size(min = 3, max = 40)
    private String name;

    private String  image;

    //@ValidPassword
    private String password;
    private String username;
    private String userTag;
    private boolean enabled;
    private String authority;

    private String salt;

    @NotEmpty
    @NotNull
    private String timezone;

    @NotNull
    @NotEmpty(message = "email is required")
    @Email(message = "Invalid email address")
    @Column(name = "email", unique = true)
    private String email;

    public TribeUser(long id, String name, String email,Boolean hasCreatedGroup) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.hasCreatedGroup = hasCreatedGroup;
    }

    public TribeUser(String name, String email,String userTag, String username) {
        this.name = name;
        this.email = email;
        this.userTag = userTag;
        this.username = username;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserTag() {
        return userTag;
    }

    public void setUserTag(String userTag) {
        this.userTag = userTag;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSalt() {return salt;}

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
