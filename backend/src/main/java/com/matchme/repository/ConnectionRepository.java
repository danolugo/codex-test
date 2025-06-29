package com.matchme.repository;

import com.matchme.model.Connection;
import com.matchme.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConnectionRepository extends JpaRepository<Connection, Long> {
    List<Connection> findByRequesterOrRecipientAndStatus(User requester, User recipient, Connection.Status status);
    Optional<Connection> findByRequesterAndRecipient(User requester, User recipient);
}
