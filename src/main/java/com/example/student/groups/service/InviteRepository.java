package com.example.student.groups.service;

import com.example.student.groups.model.Invites;
import com.example.student.groups.model.TribeGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InviteRepository extends JpaRepository<Invites, Long> {

    Optional<Invites> findInviteById(Long id);
    List<Invites> findByUserFirebaseId(String userFirebaseId);
}


