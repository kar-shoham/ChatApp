package com.shoham.chat_app.converter;

import com.shoham.chat_app.dto.ChatMessageDto;
import com.shoham.chat_app.entity.ChatMessage;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageConverter {

    public ChatMessageDto toDto(ChatMessage chatMessage) {
        if (chatMessage == null) {
            return null;
        }

        return ChatMessageDto.builder()
                .id(chatMessage.getId())
                .createdOn(chatMessage.getCreatedOn())
                .modifiedOn(chatMessage.getModifiedOn())
                .message(chatMessage.getMessage())
                .senderId(chatMessage.getSender() != null ? chatMessage.getSender().getId() : null)
                .groupId(chatMessage.getGroup() != null ? chatMessage.getGroup().getId() : null)
                .build();
    }

    public ChatMessage toEntity(ChatMessageDto chatMessageDto) {
        if (chatMessageDto == null) {
            return null;
        }

        return ChatMessage.builder()
                .id(chatMessageDto.getId())
                .createdOn(chatMessageDto.getCreatedOn())
                .modifiedOn(chatMessageDto.getModifiedOn())
                .message(chatMessageDto.getMessage())
                .build();
    }
}
