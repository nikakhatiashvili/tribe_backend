package com.example.student.habit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HabitRepository extends JpaRepository<TribeHabit, Long> {

    List<TribeHabit> findByGroupId(long groupId);
}
