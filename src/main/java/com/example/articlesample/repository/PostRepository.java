package com.example.articlesample.repository;

import com.example.articlesample.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {


    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<Post> findByTitleContainingOrContentsContaining(@Param("title") String title, @Param("contents") String contents);
}
