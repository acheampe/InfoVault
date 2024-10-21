package com.infovault.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.infovault.model.User;
import java.util.Optional;

@Repository // marks class as a repository - used as a point of data abstraction
public interface UserRepository extends JpaRepository<User, Long> {

   Optional<User> findByEmail(String email);
   Optional<User> findByCognitoUsername(String cognitoUsername);
   Optional<User> findByIsCognitoUser(Boolean isCognitoUser);
   
}