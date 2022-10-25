package com.unknown.repository;

import com.unknown.model.User;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@CacheConfig(cacheNames = "users")
public interface UserRepository extends JpaRepository<User,Long> {
    @Cacheable
    User findById(long id);
    @Cacheable
    User findByEmail(String email);
    @Cacheable
    User deleteUserById(long id);
}

