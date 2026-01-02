package com.shoham.chat_app.converter;

import com.shoham.chat_app.dto.GroupDto;
import com.shoham.chat_app.entity.Group;
import com.shoham.chat_app.entity.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class GroupConverter {

    public GroupDto toDto(Group group) {
        if (group == null) {
            return null;
        }

        return GroupDto.builder()
                .id(group.getId())
                .createdOn(group.getCreatedOn())
                .modifiedOn(group.getModifiedOn())
                .groupName(group.getGroupName())
                .groupCode(group.getGroupCode())
                .ownerId(group.getOwner() != null ? group.getOwner().getId() : null)
                .userIds(group.getUsers() != null
                        ? group.getUsers().stream()
                        .map(User::getId)
                        .collect(Collectors.toSet())
                        : null)
                .active(group.isActive())
                .build();
    }

    public Group toEntity(GroupDto groupDto) {
        if (groupDto == null) {
            return null;
        }

        return Group.builder()
                .id(groupDto.getId())
                .createdOn(groupDto.getCreatedOn())
                .modifiedOn(groupDto.getModifiedOn())
                .groupName(groupDto.getGroupName())
                .groupCode(groupDto.getGroupCode())
                .active(groupDto.isActive())
                .build();
    }
}
