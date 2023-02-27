package com.example.student.student.domain;

import com.example.student.student.TribeUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<TribeUser, Long> {

    Optional<TribeUser> findUserByFirebaseId(String firebaseId);

    Optional<TribeUser> getUserByEmail(String email);

    List<TribeUser> findByGroupsContaining(Long groupId);

    @Query("SELECT u FROM TribeUser u WHERE u.firebaseId IN :firebaseIds")
    List<TribeUser> findByFirebaseIds(@Param("firebaseIds") List<String> firebaseIds);

//    List<TribeUser> findByGroupId(Long groupId);
}
