package com.javajags.blogapp.repository;

import com.javajags.blogapp.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category, Integer> {

}
