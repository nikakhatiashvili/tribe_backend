package com.example.student.groups.service;

import com.example.student.groups.model.TribeGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<TribeGroup, Long> { }
