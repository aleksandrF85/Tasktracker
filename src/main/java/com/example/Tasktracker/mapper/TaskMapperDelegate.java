package com.example.Tasktracker.mapper;

import com.example.Tasktracker.dto.request.UpsertTaskRequest;
import com.example.Tasktracker.dto.response.TaskResponse;
import com.example.Tasktracker.model.Task;
import com.example.Tasktracker.model.TaskStatus;
import com.example.Tasktracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

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
        task.setAuthor(service.findById(request.getAuthorId()).block());
        task.setAssignee(service.findById(request.getAssigneeId()).block());
        task.setName(request.getName());
        task.setStatus(TaskStatus.TODO);
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
        response.setAuthor(mapper.userToResponse(service.findById(task.getAuthorId()).block()));
        response.setAssignee(mapper.userToResponse(service.findById(task.getAssigneeId()).block()));
        response.setCreatedAt(task.getCreatedAt());
        response.setUpdatedAt(task.getUpdatedAt());
        response.setObservers(task.getObserverIds().stream()
                .map(i -> mapper.userToResponse(service.findById(i).block())).collect(Collectors.toSet()));
        response.setStatus(task.getStatus());

        return response;
    }
}
