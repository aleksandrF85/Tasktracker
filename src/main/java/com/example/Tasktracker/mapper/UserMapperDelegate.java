package com.example.Tasktracker.mapper;

import com.example.Tasktracker.dto.request.UpsertUserRequest;
import com.example.Tasktracker.dto.response.UserResponse;
import com.example.Tasktracker.model.User;

public abstract class UserMapperDelegate implements UserMapper{

    @Override
    public User requestToUser(UpsertUserRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        return user;
    }

    @Override
    public User requestToUser(String id, UpsertUserRequest request) {
        User user = requestToUser(request);
        user.setId(id);

        return user;
    }

    @Override
    public UserResponse userToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());

        return response;
    }
}
