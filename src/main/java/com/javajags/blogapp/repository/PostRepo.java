package com.javajags.blogapp.repository;

import com.javajags.blogapp.entity.Category;
import com.javajags.blogapp.entity.Post;
import com.javajags.blogapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepo extends JpaRepository<Post, Integer> {

    List<Post> findByUser(User user);
    List<Post> findByCategory(Category category);
    @Query("select p from Post p where p.title like :key")
    List<Post> searchByTitle(@Param("key") String title);

}
