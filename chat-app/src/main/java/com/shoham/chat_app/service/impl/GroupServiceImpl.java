package com.shoham.chat_app.service.impl;

import com.shoham.chat_app.converter.GroupConverter;
import com.shoham.chat_app.dto.GroupDto;
import com.shoham.chat_app.entity.Group;
import com.shoham.chat_app.entity.User;
import com.shoham.chat_app.exception.InvalidInputException;
import com.shoham.chat_app.exception.UnauthorizedOperationException;
import com.shoham.chat_app.exception.UserAlreadyInGroupException;
import com.shoham.chat_app.exception.UserNotInGroupException;
import com.shoham.chat_app.repository.GroupRepository;
import com.shoham.chat_app.service.GroupService;
import com.shoham.chat_app.service.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl
        implements GroupService
{
    @Autowired
    private GroupRepository repository;

    @Autowired
    private GroupConverter converter;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    @Override
    public List<GroupDto> list()
    {
        List<Group> entities = repository.findAll();
        return entities.stream().map(entity -> converter.toDto(entity)).collect(Collectors.toList());
    }

    @Override
    public Group getById(@NonNull Long id)
    {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Group with id=" + id + " not found!"));
    }

    @Override
    public GroupDto get(@NonNull Long id)
    {
        Group entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Group with id=" + id + " not found!"));
        return converter.toDto(entity);
    }

    @Override
    public GroupDto create(@NonNull GroupDto groupDto)
    {
        User loggedInUser = authenticationService.getUser();

        if (!StringUtils.hasText(groupDto.getGroupCode()) || !StringUtils.hasText(groupDto.getGroupName())) {
            throw new InvalidInputException("Some of the mandatory fields are missing!");
        }
        if (repository.findGroupByGroupCode(groupDto.getGroupCode()).isPresent()) {
            throw new EntityExistsException("Group Code: " + groupDto.getGroupCode() + " already exists!");
        }

        Group group = converter.toEntity(groupDto);
        group.setOwner(loggedInUser);
        group = repository.save(group);
        return converter.toDto(group);
    }

    @Override
    public GroupDto update(
            @NonNull Long id,
            @NonNull GroupDto groupDto)
    {
        User loggedInUser = authenticationService.getUser();

        Group dbGroup = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Group with id=" + id + " not found!"));

        if (!loggedInUser.getId().equals(dbGroup.getOwner().getId())) {
            throw new UnauthorizedOperationException("Only Group Owner can update!");
        }

        // only group name can be updated
        dbGroup.setGroupName(groupDto.getGroupName());

        dbGroup = repository.save(dbGroup);
        return converter.toDto(dbGroup);
    }

    @Override
    @Transactional
    public List<Long> addUser(@NonNull Long id)
    {
        Group dbGroup = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Group with id=" + id + " not found!"));
        Set<User> joined = dbGroup.getUsers();

        User user = authenticationService.getUser();

        if (joined.contains(user)) {
            throw new UserAlreadyInGroupException("User already present in Group Id:" + id);
        }
        joined.add(user);

        return joined.stream().map(x -> x.getId()).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<Long> removeUser(@NonNull Long id)
    {
        Group dbGroup = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Group with id=" + id + " not found!"));
        Set<User> joined = dbGroup.getUsers();

        User user = authenticationService.getUser();

        if (!joined.contains(user)) {
            throw new UserNotInGroupException("User NOT present in Group Id:" + id);
        }
        joined.remove(user);

        return joined.stream().map(x -> x.getId()).collect(Collectors.toList());
    }

    @Override
    public void delete(@NonNull Long id)
    {
        User loggedInUser = authenticationService.getUser();

        Group dbGroup = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Group with id=" + id + " not found!"));

        if (!loggedInUser.getId().equals(dbGroup.getOwner().getId())) {
            throw new UnauthorizedOperationException("Only Group Owner can delete!");
        }

        repository.delete(dbGroup);
    }
}
