package com.shoham.chat_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class BaseEntityDto
{
    private Long id;
    private LocalDateTime createdOn;
    private LocalDateTime modifiedOn;
}
