package com.example.student.tasks.domain;

import com.example.student.tasks.model.TribeTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TribeTask, Long> {

    List<TribeTask> findByGroupId(long groupId);
}
