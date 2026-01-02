package com.shoham.chat_app.service;

import com.shoham.chat_app.entity.User;
import lombok.NonNull;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService
    extends UserDetailsService
{
    User getById(@NonNull Long id);

    User create(@NonNull User user);

    List<User> getAll();

    User updateName(@NonNull Long id, @NonNull String name, @NonNull String currentUsername);
}
