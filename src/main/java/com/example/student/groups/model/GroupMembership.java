package com.example.student.groups.model;

import com.example.student.student.TribeUser;
import jakarta.persistence.*;

@Entity
@Table(name = "group_membership")
public class GroupMembership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private TribeUser user;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private TribeGroup group;

    public TribeUser getUser() {
        return user;
    }

    public void setUser(TribeUser user) {
        this.user = user;
    }

    public TribeGroup getGroup() {
        return group;
    }

    public void setGroup(TribeGroup group) {
        this.group = group;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
