package com.infovault.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.infovault.model.User;

@Repository // marks class as a repository
public interface UserRepository extends JpaRepository<User, Long> {
   // User findByEmail(String email);
   //additional query methods can be defined here later
}