package com.example.student.groups.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table
public class Invites {
    @Id
    @SequenceGenerator(name = "invite_sequence", sequenceName = "invite_sequence", allocationSize = 1)
    @GeneratedValue(
            strategy = GenerationType.IDENTITY,
            generator = "invite_sequence"
    )
    private long id;

    private long groupId;
    @NotNull
    @Size(min = 10, max = 100)
    private String userFirebaseId;
    @NotNull
    @Size(min = 3, max = 40)
    private String tribeName;

    @NotNull
    @Size(min = 10, max = 100)
    private String tribeDescription;

    public Invites(String tribeName, String tribeDescription, String userFirebaseId,Long groupId) {
        this.tribeName = tribeName;
        this.tribeDescription = tribeDescription;
        this.userFirebaseId = userFirebaseId;
        this.groupId = groupId;
    }
    public Invites(){

    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserFirebaseId() {
        return userFirebaseId;
    }

    public void setUserFirebaseId(String userFirebaseId) {
        this.userFirebaseId = userFirebaseId;
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
}
