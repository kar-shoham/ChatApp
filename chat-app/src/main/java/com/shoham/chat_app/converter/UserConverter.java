package com.shoham.chat_app.converter;

import com.shoham.chat_app.dto.UserDto;
import com.shoham.chat_app.entity.Group;
import com.shoham.chat_app.entity.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserConverter {

    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }

        return UserDto.builder()
                .id(user.getId())
                .createdOn(user.getCreatedOn())
                .modifiedOn(user.getModifiedOn())
                .username(user.getUsername())
                .name(user.getName())
                .groupIds(user.getGroups() != null
                        ? user.getGroups().stream()
                        .map(Group::getId)
                        .collect(Collectors.toSet())
                        : null)
                .build();
    }

    public User toEntity(UserDto userDto) {
        if (userDto == null) {
            return null;
        }

        return User.builder()
                .id(userDto.getId())
                .createdOn(userDto.getCreatedOn())
                .modifiedOn(userDto.getModifiedOn())
                .username(userDto.getUsername())
                .name(userDto.getName())
                .build();
    }
}
