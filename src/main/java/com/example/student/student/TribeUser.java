package com.example.student.student;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table
public class TribeUser {

    @Id
    @SequenceGenerator(name = "student_sequence",sequenceName = "student_sequence",allocationSize = 1)
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "student_sequence"
    )
    private long id;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long groupId;


    @NotNull
    @Size(min = 10, max = 100)
    private String firebaseId;

    @NotNull
    @Size(min = 3, max = 40)
    private String name;
    @NotNull

    @NotEmpty(message = "email is required")
    @Email(message = "Invalid email address")
    private String email;


    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public TribeUser(long id, String name , String email, String firebaseId) {
        this.firebaseId = firebaseId;
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public TribeUser(String name, String email, String firebaseId) {
        this.firebaseId = firebaseId;
        this.name = name;
        this.email = email;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firebase_id='" + firebaseId +
                ", groupId='" + groupId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
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
    public TribeUser(){


    }
}
