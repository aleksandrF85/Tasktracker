package com.example.Tasktracker.mapper;

import com.example.Tasktracker.dto.request.UpsertTaskRequest;
import com.example.Tasktracker.dto.response.TaskResponse;
import com.example.Tasktracker.model.Task;
import com.example.Tasktracker.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;


@Slf4j
public abstract class TaskMapperDelegate implements TaskMapper {

    @Autowired
    private UserService service;

    @Autowired
    private UserMapper mapper;


    @Override
    public Task requestToTask(UpsertTaskRequest request) {
        Task task = new Task();
        task.setAuthorId(request.getAuthorId());
        task.setAssigneeId(request.getAssigneeId());
        task.setDescription(request.getDescription());
        task.setName(request.getName());
        task.setStatus(request.getStatus());
        return task;
    }

    @Override
    public Task requestToTask(String id, UpsertTaskRequest request) {
        Task task = requestToTask(request);
        task.setId(id);
        return task;
    }

    @Override
    public TaskResponse taskToResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setName(task.getName());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus());
        response.setCreatedAt(task.getCreatedAt());
        response.setUpdatedAt(task.getUpdatedAt());
        response.setAuthor(mapper.userToResponse(task.getAuthor()));
        response.setAssignee(mapper.userToResponse(task.getAssignee()));
        response.setObservers(mapper.userSetToUserResponseSet(task.getObservers()));

        return response;
    }
}
