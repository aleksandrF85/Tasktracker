package com.example.Tasktracker.mapper;

import com.example.Tasktracker.dto.request.UpsertUserRequest;
import com.example.Tasktracker.dto.response.UserResponse;
import com.example.Tasktracker.model.User;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@DecoratedWith(UserMapperDelegate.class)
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User requestToUser(UpsertUserRequest request);

    User requestToUser(String id, UpsertUserRequest request);

    UserResponse userToResponse(User user);

}
