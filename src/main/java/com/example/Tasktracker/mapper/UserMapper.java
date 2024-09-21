package com.example.Tasktracker.mapper;

import com.example.Tasktracker.dto.request.UpsertUserRequest;
import com.example.Tasktracker.dto.response.UserResponse;
import com.example.Tasktracker.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User requestToUser(UpsertUserRequest request);

    User requestToUser(String id, UpsertUserRequest request);

    UserResponse userToResponse(User user);

//    default UserListResponse userListToUserResponseList(List<User> users) {
//        UserListResponse response = new UserListResponse();
//
//        response.setUsers(users.stream()
//                .map(this::userToResponse).collect(Collectors.toList()));
//
//        return response;
//    }
}
