package com.shoham.chat_app.service.impl;

import com.shoham.chat_app.dto.AuthRequestDto;
import com.shoham.chat_app.dto.AuthResponseDto;
import com.shoham.chat_app.entity.User;
import com.shoham.chat_app.service.AuthService;
import com.shoham.chat_app.service.UserService;
import com.shoham.chat_app.utils.JwtUtils;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl
        implements AuthService
{
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @Override
    public AuthResponseDto login(@NonNull AuthRequestDto requestDto)
    {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        User user = (User) authentication.getPrincipal();

        String token = jwtUtils.getToken(user);

        return AuthResponseDto.builder()
                .username(user.getUsername())
                .token(token)
                .build();
    }

    @Override
    public AuthResponseDto signin(@NonNull AuthRequestDto requestDto)
    {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        User user = User.builder()
                .username(requestDto.getUsername())
                .name(requestDto.getName())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .build();
        user = userService.create(user);

        String token = jwtUtils.getToken(user);

        return AuthResponseDto.builder()
                .username(user.getUsername())
                .token(token)
                .build();
    }
}
