package com.infovault.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.infovault.model.User;
import java.util.Optional;

@Repository // marks class as a repository
public interface UserRepository extends JpaRepository<User, Long> {

   Optional<User> findByEmail(String email);
   //additional query methods can be defined here later
}