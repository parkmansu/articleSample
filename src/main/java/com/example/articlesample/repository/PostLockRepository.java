package com.example.articlesample.repository;

import com.example.articlesample.model.Post;
import com.example.articlesample.model.PostLock;
import com.example.articlesample.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLockRepository extends JpaRepository<PostLock, Long> {
    PostLock findByUsersAndPost(Users users, Post post);

    PostLock findByPost(Post post);

}
