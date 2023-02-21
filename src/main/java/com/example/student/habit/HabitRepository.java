package com.example.student.habit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HabitRepository extends JpaRepository<TribeHabit, Long> {

//    Optional<TribeHabit> findByGroupIdAndId(long groupId, long id);
//
//    List<TribeHabit> findByGroupId(long groupId);
}
