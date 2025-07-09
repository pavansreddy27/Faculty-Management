package com.university.fms.repository;

import com.university.fms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username); // Used by UserDetailsServiceImpl
    Optional<User> findByEmail(String email);
    Boolean existsByUsername(String username); // Used by UserService.createUser

    Boolean existsByEmail(String email); // Used by UserService.createUser
    

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
    List<User> findByRoleName(String roleName); // Used by UserService.getUsersByRole
}