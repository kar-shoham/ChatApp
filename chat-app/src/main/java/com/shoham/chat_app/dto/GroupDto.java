package com.shoham.chat_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class GroupDto
        extends BaseEntityDto
{
    private String groupName;
    private String groupCode;
    private Long ownerId;
    private Set<Long> userIds;
    private boolean active;
}
