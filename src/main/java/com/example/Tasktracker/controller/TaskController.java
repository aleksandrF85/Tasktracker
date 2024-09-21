package com.example.Tasktracker.controller;

import com.example.Tasktracker.dto.request.UpsertTaskRequest;
import com.example.Tasktracker.dto.response.TaskResponse;
import com.example.Tasktracker.mapper.TaskMapper;
import com.example.Tasktracker.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    private final TaskMapper taskMapper;

    @GetMapping
    public Flux<TaskResponse> findAll() {
        return taskService.findAll().map(taskMapper::taskToResponse);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<TaskResponse>> findById(@PathVariable String id) {
        return taskService.findById(id).map(taskMapper::taskToResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<TaskResponse>> create(@RequestBody @Valid UpsertTaskRequest request) {
        return taskService.save(taskMapper.requestToTask(request))
                .map(taskMapper::taskToResponse)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/{id}")
    public Mono<ResponseEntity<TaskResponse>> update(@PathVariable String id, @RequestBody @Valid UpsertTaskRequest request) {
        return taskService.update(id, taskMapper.requestToTask(id, request))
                .map(taskMapper::taskToResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/observer/{id}")
    public Mono<ResponseEntity<TaskResponse>> setObserver(@PathVariable String id, @RequestParam String userId) {
        return taskService.setObserver(id, userId)
                .map(taskMapper::taskToResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable String id) {
        return taskService.deleteById(id).then(Mono.just(ResponseEntity.noContent().build()));
    }


}
