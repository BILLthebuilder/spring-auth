package com.spring.auth.repository;

import com.spring.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findById(long id);
    User findByEmail(String email);
    User deleteUserBy(long id);
}
