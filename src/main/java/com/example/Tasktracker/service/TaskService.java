package com.example.Tasktracker.service;

import com.example.Tasktracker.model.Task;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface TaskService {

    Flux<Task> findAll();

    Mono<Task> findById(String id);

    Mono<Task> save(Task task);

    Mono<Task> update(String id, Task task);

    Mono<Void> deleteById(String id);

    Mono<Task> setObserver(String taskId, String userId);

    Mono<Task> deleteObservers(String taskId);

}
