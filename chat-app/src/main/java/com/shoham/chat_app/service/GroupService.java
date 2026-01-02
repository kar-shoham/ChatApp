package com.shoham.chat_app.service;

import com.shoham.chat_app.dto.GroupDto;
import com.shoham.chat_app.entity.Group;
import lombok.NonNull;

import java.util.List;

public interface GroupService
{
    List<GroupDto> list();

    Group getById(@NonNull Long id);

    GroupDto get(@NonNull Long id);

    GroupDto create(@NonNull GroupDto groupDto);

    GroupDto update(
            @NonNull Long id,
            @NonNull GroupDto groupDto);

    List<Long> addUser(@NonNull Long id);

    List<Long> removeUser(@NonNull Long id);

    void delete(@NonNull Long id);
}
