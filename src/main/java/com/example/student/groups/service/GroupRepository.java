package com.example.student.groups.service;

import com.example.student.groups.model.TribeGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<TribeGroup, Long> {
    Optional<TribeGroup> getGroupById(Long id);

    Optional<TribeGroup> getGroupByAdminId(Long adminId);
}
