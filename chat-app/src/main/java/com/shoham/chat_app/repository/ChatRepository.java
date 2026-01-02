package com.shoham.chat_app.repository;

import com.shoham.chat_app.dto.ChatMessageDto;
import com.shoham.chat_app.entity.ChatMessage;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository
        extends JpaRepository<ChatMessage, Long>
{
    @Query("FROM ChatMessage c WHERE c.group.id = :groupId ORDER BY c.createdOn DESC")
    List<ChatMessage> findByGroupId(@NonNull @Param("groupId") Long groupId);
}