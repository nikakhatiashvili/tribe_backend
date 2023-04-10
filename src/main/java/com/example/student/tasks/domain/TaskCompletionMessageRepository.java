package com.example.student.tasks.domain;

import com.example.student.tasks.model.TaskCompletionMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskCompletionMessageRepository extends JpaRepository<TaskCompletionMessage, Long> {

    Optional<TaskCompletionMessage> findByTaskIdAndUserIdAndStrDateAndGroupId(long taskId, long userId, String strDate, Long groupId);

    List<TaskCompletionMessage> findByUserIdAndStrDate(long userId, String strDate);

    Optional<TaskCompletionMessage> findByUserIdAndStrDateAndTaskId(long userId, String strDate, Long taskId);
    List<TaskCompletionMessage> findAllByGroupId(Long id);
}
