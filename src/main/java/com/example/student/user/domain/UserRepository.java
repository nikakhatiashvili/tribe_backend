package com.example.student.user.domain;

import com.example.student.user.TribeUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<TribeUser, Long> {

    Optional<TribeUser> findUserById(Long id);

    Optional<TribeUser> getUserByEmail(String email);

    List<TribeUser> findByGroupsContaining(Long groupId);

}
