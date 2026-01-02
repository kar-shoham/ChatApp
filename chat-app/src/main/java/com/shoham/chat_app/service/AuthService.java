package com.shoham.chat_app.service;

import com.shoham.chat_app.dto.AuthRequestDto;
import com.shoham.chat_app.dto.AuthResponseDto;
import lombok.NonNull;

public interface AuthService
{
    AuthResponseDto login(@NonNull AuthRequestDto requestDto);

    AuthResponseDto signin(@NonNull AuthRequestDto requestDto);
}
