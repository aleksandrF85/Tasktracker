package com.example.Tasktracker.mapper;

import com.example.Tasktracker.dto.request.UpsertTaskRequest;
import com.example.Tasktracker.dto.response.TaskResponse;
import com.example.Tasktracker.dto.response.UserResponse;
import com.example.Tasktracker.model.Task;
import com.example.Tasktracker.model.User;
import com.example.Tasktracker.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;


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

        service.findById(task.getAuthorId()).subscribe(author -> {
            log.info(author.toString());
            response.setAuthor(mapper.userToResponse(author));
        });
        service.findById(task.getAssigneeId()).subscribe(assignee -> {
            log.info(assignee.toString());
            response.setAssignee(mapper.userToResponse(assignee));
        });
        Set<UserResponse> set = new HashSet<>();

        task.getObserverIds().forEach(o -> {
            service.findById(o).subscribe(observer -> set.add(mapper.userToResponse(observer)));
            response.setObservers(set);
        });

//        response.setAuthor(mapper.userToResponse(service.findById(task.getAuthorId()).block()));
//        response.setAssignee(mapper.userToResponse(service.findById(task.getAssigneeId()).block()));
//        response.setObservers(task.getObserverIds().stream()
//                .map(i -> mapper.userToResponse(service.findById(i).block())).collect(Collectors.toSet()));

//        Mono.zip(Mono.just(task), service.findById(task.getAuthorId()),  service.findById(task.getAssigneeId())).flatMap(data ->{
//            TaskResponse response = new TaskResponse();
//            response.setId(data.getT1().getId());
//            response.setName(data.getT1().getName());
//            response.setDescription(data.getT1().getDescription());
//            response.setStatus(data.getT1().getStatus());
//            response.setCreatedAt(data.getT1().getCreatedAt());
//            response.setUpdatedAt(data.getT1().getUpdatedAt());
//            response.setAuthor(mapper.userToResponse(data.getT2()));
//            response.setAssignee(mapper.userToResponse(data.getT3()));
//
//            return Mono.just(response);

        log.info(response.toString());

        return response;
    }
}
