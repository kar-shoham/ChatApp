package com.shoham.chat_app.controller;

import com.shoham.chat_app.dto.ChatMessageDto;
import com.shoham.chat_app.service.ChatMessageService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ChatMessageController
{
    @Autowired
    private ChatMessageService service;

    @GetMapping("/{groupId}/chats")
    public ResponseEntity<List<ChatMessageDto>> get(@PathVariable @NonNull Long groupId)
    {
        return ResponseEntity.ok(service.list(groupId));
    }

    @PutMapping("/{groupId}/chats/{chatMessageId}")
    public ResponseEntity<ChatMessageDto> updateMessage(
            @PathVariable @NonNull Long groupId,
            @PathVariable @NonNull Long chatMessageId,
            @RequestBody @NonNull ChatMessageDto chatMessageDto)
    {
        return ResponseEntity.ok(service.update(groupId, chatMessageId, chatMessageDto));
    }

    @DeleteMapping("/{groupId}/chats/{chatMessageId}")
    public ResponseEntity<Boolean> deleteMessage(
            @PathVariable @NonNull Long groupId,
            @PathVariable @NonNull Long chatMessageId)
    {
        service.delete(groupId, chatMessageId);
        return ResponseEntity.ok(true);
    }
}
