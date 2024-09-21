package com.example.Tasktracker.dto.response;

import com.example.Tasktracker.model.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {

    String id;

    String name;

    String description;

    Instant createdAt;

    Instant updatedAt;

    TaskStatus status;

    UserResponse author;

    UserResponse assignee;

    Set<UserResponse> observers;

}
