package com.example.student.tasks;

import com.example.student.tasks.model.CompletedTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompletedTaskRepository extends JpaRepository<CompletedTask, Long> {

    List<CompletedTask> findAllByUserId(String userId);
}
