package com.shoham.chat_app.controller;

import com.shoham.chat_app.dto.GroupDto;
import com.shoham.chat_app.service.GroupService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups")
public class GroupController
{
    @Autowired
    private GroupService service;

    @GetMapping
    public ResponseEntity<List<GroupDto>> list()
    {
        List<GroupDto> dtos = service.list();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<GroupDto> get(@PathVariable @NonNull Long groupId)
    {
        GroupDto dto = service.get(groupId);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<GroupDto> create(@RequestBody @NonNull GroupDto groupDto)
    {
        GroupDto dto = service.create(groupDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/{groupId}")
    public ResponseEntity<GroupDto> update(
            @PathVariable @NonNull Long groupId,
            @RequestBody @NonNull GroupDto groupDto)
    {
        GroupDto dto = service.update(groupId, groupDto);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{groupId}/join")
    public ResponseEntity<List<Long>> joinGroup(@PathVariable @NonNull Long groupId)
    {
        List<Long> userIds = service.addUser(groupId);
        return ResponseEntity.ok(userIds);
    }

    @PostMapping("/{groupId}/leave")
    public ResponseEntity<List<Long>> leaveGroup(@PathVariable @NonNull Long groupId)
    {
        List<Long> userIds = service.removeUser(groupId);
        return ResponseEntity.ok(userIds);
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<Boolean> delete(@PathVariable @NonNull Long groupId)
    {
        service.delete(groupId);
        return ResponseEntity.status(HttpStatus.OK).body(true);
    }
}
