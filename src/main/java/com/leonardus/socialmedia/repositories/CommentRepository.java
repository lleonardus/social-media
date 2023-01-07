package com.leonardus.socialmedia.repositories;

import com.leonardus.socialmedia.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
