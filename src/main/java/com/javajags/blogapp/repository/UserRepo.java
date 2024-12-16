package com.javajags.blogapp.repository;

import com.javajags.blogapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Integer> {
}
