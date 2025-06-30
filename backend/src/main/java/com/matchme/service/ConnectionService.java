package com.matchme.service;

import com.matchme.model.Connection;
import com.matchme.model.User;
import com.matchme.repository.ConnectionRepository;
import com.matchme.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ConnectionService {
    private final ConnectionRepository connectionRepository;
    private final UserRepository userRepository;

    public ConnectionService(ConnectionRepository connectionRepository, UserRepository userRepository) {
        this.connectionRepository = connectionRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void requestConnection(User requester, Long targetId) {
        User target = userRepository.findById(targetId).orElseThrow();
        if (connectionRepository.findByRequesterAndRecipient(requester, target).isPresent()) {
            return; // already requested
        }
        Connection connection = new Connection();
        connection.setRequester(requester);
        connection.setRecipient(target);
        connection.setStatus(Connection.Status.PENDING);
        connectionRepository.save(connection);
    }

    @Transactional
    public void acceptConnection(User recipient, Long connectionId) {
        Connection connection = connectionRepository.findByIdAndRecipient(connectionId, recipient)
                .orElseThrow();
        connection.setStatus(Connection.Status.CONNECTED);
        connectionRepository.save(connection);
    }

    @Transactional
    public void removeConnection(User user, Long connectionId) {
        Connection connection = connectionRepository.findById(connectionId).orElseThrow();
        if (!connection.getRequester().equals(user) && !connection.getRecipient().equals(user)) {
            throw new IllegalArgumentException("Not your connection");
        }
        connectionRepository.delete(connection);
    }

    public List<Map<String, Object>> getIncomingRequests(User user) {
        return connectionRepository.findByRecipientAndStatus(user, Connection.Status.PENDING)
                .stream()
                .map(c -> Map.of("id", c.getId(), "requesterId", c.getRequester().getId()))
                .collect(Collectors.toList());
    }

    public List<Long> getConnections(User user) {
        return connectionRepository
                .findByRequesterOrRecipientAndStatus(user, user, Connection.Status.CONNECTED)
                .stream()
                .map(c -> c.getRequester().equals(user) ? c.getRecipient().getId() : c.getRequester().getId())
                .collect(Collectors.toList());
    }
}
