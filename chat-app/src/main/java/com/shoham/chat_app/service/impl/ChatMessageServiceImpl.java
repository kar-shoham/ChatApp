package com.shoham.chat_app.service.impl;

import com.shoham.chat_app.converter.ChatMessageConverter;
import com.shoham.chat_app.dto.ChatMessageDto;
import com.shoham.chat_app.entity.ChatMessage;
import com.shoham.chat_app.entity.Group;
import com.shoham.chat_app.entity.User;
import com.shoham.chat_app.exception.InvalidInputException;
import com.shoham.chat_app.repository.ChatRepository;
import com.shoham.chat_app.service.ChatMessageService;
import com.shoham.chat_app.service.GroupService;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatMessageServiceImpl
        implements ChatMessageService
{
    @Autowired
    private ChatRepository repository;

    @Autowired
    private ChatMessageConverter converter;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private GroupService groupService;

    @Override
    public List<ChatMessageDto> list(@NonNull Long groupId)
    {
        Group group = groupService.getById(groupId);
        List<ChatMessage> chatMessages = repository.findByGroupId(group.getId());
        return chatMessages.stream()
                .map(chatMessage -> converter.toDto(chatMessage)).collect(Collectors.toList());
    }

    @Override
    public ChatMessageDto create(
            @NonNull Long groupId,
            @NonNull String message)
    {
        User loggedInUser = authenticationService.getUser();
        Group group = groupService.getById(groupId);

        ChatMessage chatMessage = ChatMessage.builder()
                .group(group)
                .message(message)
                .sender(loggedInUser)
                .build();

        chatMessage = repository.save(chatMessage);
        return converter.toDto(chatMessage);
    }

    @Override
    public ChatMessageDto update(
            @NonNull Long groupId,
            @NonNull Long chatMessageId,
            @NonNull ChatMessageDto chatMessageDto)
    {
        ChatMessage dbChatMessage = repository.findById(chatMessageId).orElseThrow(() ->
                new EntityNotFoundException("Chat Messae with id=" + chatMessageId + " not found!"));

        if (!dbChatMessage.getGroup().getId().equals(groupId)) {
            throw new EntityNotFoundException(
                    "Chat Message with id=" + chatMessageId + " does not belong to Group with id=" + groupId + "!");
        }
        if (!StringUtils.hasText(chatMessageDto.getMessage())) {
            throw new InvalidInputException("Invalid Message!");
        }

        dbChatMessage.setMessage(chatMessageDto.getMessage());
        dbChatMessage = repository.save(dbChatMessage);

        return converter.toDto(dbChatMessage);
    }

    @Override
    public void delete(
            @NonNull Long groupId,
            @NonNull Long chatMessageId)
    {
        ChatMessage dbChatMessage = repository.findById(chatMessageId).orElseThrow(() ->
                new EntityNotFoundException("Chat Messae with id=" + chatMessageId + " not found!"));

        if (!dbChatMessage.getGroup().getId().equals(groupId)) {
            throw new EntityNotFoundException(
                    "Chat Message with id=" + chatMessageId + " does not belong to Group with id=" + groupId + "!");
        }

        repository.delete(dbChatMessage);
    }
}
