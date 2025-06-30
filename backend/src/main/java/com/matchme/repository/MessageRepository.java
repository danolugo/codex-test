package com.matchme.repository;

import com.matchme.model.Message;
import com.matchme.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT m FROM Message m WHERE (m.sender = :u1 AND m.recipient = :u2) " +
           "OR (m.sender = :u2 AND m.recipient = :u1) ORDER BY m.timestamp DESC")
    List<Message> findRecentMessages(@Param("u1") User u1, @Param("u2") User u2);
}
