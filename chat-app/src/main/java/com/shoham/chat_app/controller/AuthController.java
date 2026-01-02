package com.shoham.chat_app.controller;

import com.shoham.chat_app.dto.AuthRequestDto;
import com.shoham.chat_app.dto.AuthResponseDto;
import com.shoham.chat_app.service.AuthService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AuthController
{
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody @NonNull AuthRequestDto requestDto)
    {
        AuthResponseDto responseDto = authService.login(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDto> signup(@RequestBody @NonNull AuthRequestDto requestDto)
    {
        AuthResponseDto responseDto = authService.signin(requestDto);
        return ResponseEntity.ok(responseDto);
    }
}
