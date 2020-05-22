package com.lin.missyou.repository;

import com.lin.missyou.model.dataAccessObject.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByOpenid(String openid);

    User findFirstById(Long id);
}