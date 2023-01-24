package com.example.student.groups.service;

import com.example.student.groups.model.GroupMembership;
import com.example.student.groups.model.TribeGroup;
import com.example.student.student.TribeUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMembershipRepository extends JpaRepository<GroupMembership, Long> {

}