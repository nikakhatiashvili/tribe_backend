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

    public Long getUserBaseId() {
        return userBaseId;
    }

    public void setUserBaseId(Long userBaseId) {
        this.userBaseId = userBaseId;
    }

    @NotNull
    private Long userBaseId;
    private String invitedBy;
    @NotNull
    @Size(min = 3, max = 40)
    private String tribeName;
    @NotNull
    @Size(min = 10, max = 100)
    private String tribeDescription;
    public Invites(String tribeName, String tribeDescription, Long userId, String invitedBy, Long groupId) {
        this.tribeName = tribeName;
        this.tribeDescription = tribeDescription;
        this.userBaseId = userId;
        this.invitedBy = invitedBy;
        this.groupId = groupId;
    }

    public Invites() {

    }

    public String getInvitedBy() {
        return invitedBy;
    }

    public void setInvitedBy(String invitedBy) {
        this.invitedBy = invitedBy;
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
