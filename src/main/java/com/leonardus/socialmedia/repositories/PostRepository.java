package com.leonardus.socialmedia.repositories;

import com.leonardus.socialmedia.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
