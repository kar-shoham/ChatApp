package com.shoham.chat_app.service.impl;

import com.shoham.chat_app.entity.User;
import com.shoham.chat_app.exception.InvalidInputException;
import com.shoham.chat_app.exception.UserAlreadyExistsException;
import com.shoham.chat_app.repository.UserRepository;
import com.shoham.chat_app.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl
        implements UserService
{
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException
    {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found!"));
    }

    @Override
    public User getById(@NonNull Long id)
    {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id=" + id + " not found!"));
    }

    @Override
    public User create(@NonNull User user)
    {
        User dbUser = (User) userRepository.findUserByUsername(user.getUsername()).orElse(null);
        if (Objects.nonNull(dbUser)) {
            throw new UserAlreadyExistsException("Username " + user.getUsername() + " is already taken!");
        }
        if (!StringUtils.hasText(user.getName())) {
            throw new InvalidInputException("Name is not present!");
        }
        if (Objects.nonNull(user.getId())) {
            throw new InvalidInputException("Id already present!");
        }

        return userRepository.save(user);
    }

    @Override
    public List<User> getAll()
    {
        return userRepository.findAll();
    }

    @Override
    public User updateName(
            @NonNull Long id,
            @NonNull String name,
            @NonNull String currentUsername)
    {
        User user = getById(id);

        if (!user.getUsername().equals(currentUsername)) {
            throw new AccessDeniedException("You can only update your own profile!");
        }

        if (!StringUtils.hasText(name)) {
            throw new InvalidInputException("Name cannot be empty!");
        }

        user.setName(name);
        return userRepository.save(user);
    }
}
