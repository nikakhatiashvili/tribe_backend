package com.example.student.tasks;

import com.example.student.tasks.model.CompletedTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TribeTask, Long> {

    List<TribeTask> findByGroupIdAndAssignedToContaining(Long groupId, String firebaseId);
    List<TribeTask> findByGroupId(long groupId);
}
