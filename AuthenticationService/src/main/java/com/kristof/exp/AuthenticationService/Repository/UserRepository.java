package com.kristof.exp.AuthenticationService.Repository;

import com.kristof.exp.AuthenticationService.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select user from User user where user.username = :username")
    Optional<User> findUserByName(String username);
}
