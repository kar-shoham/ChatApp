package com.shoham.chat_app.controller;

import com.shoham.chat_app.converter.UserConverter;
import com.shoham.chat_app.dto.UserDto;
import com.shoham.chat_app.entity.User;
import com.shoham.chat_app.service.UserService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserConverter userConverter;

    @GetMapping
    public ResponseEntity<List<UserDto>> list() {
        List<User> users = userService.getAll();
        List<UserDto> userDtos = users.stream()
                .map(userConverter::toDto)
                .toList();
        return ResponseEntity.ok(userDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> get(@PathVariable @NonNull Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userConverter.toDto(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(
            @PathVariable @NonNull Long id,
            @RequestBody @NonNull UserDto userDto,
            Authentication authentication) {
        String currentUsername = authentication.getName();
        User updatedUser = userService.updateName(id, userDto.getName(), currentUsername);
        return ResponseEntity.ok(userConverter.toDto(updatedUser));
    }
}
