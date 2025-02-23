package com.softuni.project.user.repository;

import com.softuni.project.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
//    @Query("SELECT u FROM User u where u.username =:username")
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findAllByIdIsNot(UUID userId);
}
