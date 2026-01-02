package com.shoham.chat_app.service;

import com.shoham.chat_app.dto.ChatMessageDto;
import lombok.NonNull;

import java.util.List;

public interface ChatMessageService
{
    List<ChatMessageDto> list(@NonNull Long groupId);

    ChatMessageDto create(
            @NonNull Long groupId,
            @NonNull String message);

    ChatMessageDto update(
            @NonNull Long groupId,
            @NonNull Long chatMessageId,
            @NonNull ChatMessageDto chatMessageDto);

    void delete(
            @NonNull Long groupId,
            @NonNull Long chatMessageId);
}
