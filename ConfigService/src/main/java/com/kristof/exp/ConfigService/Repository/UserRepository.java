package com.kristof.exp.ConfigService.Repository;

import com.kristof.exp.ConfigService.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select user from User user where user.username = :username")
    User findUserByName(String username);
}
