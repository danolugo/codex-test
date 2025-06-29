package com.matchme.service;

import com.matchme.model.Message;
import com.matchme.model.User;
import com.matchme.repository.MessageRepository;
import com.matchme.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChatService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public ChatService(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Message saveMessage(User sender, Long recipientId, String content) {
        User recipient = userRepository.findById(recipientId).orElseThrow();
        Message msg = new Message();
        msg.setSender(sender);
        msg.setRecipient(recipient);
        msg.setContent(content);
        return messageRepository.save(msg);
    }

    public List<Message> getRecentMessages(User a, Long otherId) {
        User b = userRepository.findById(otherId).orElseThrow();
        return messageRepository.findRecentMessages(a, b);
    }
}
