package com.example.Tasktracker.service.impl;

import com.example.Tasktracker.model.Task;
import com.example.Tasktracker.repository.TaskRepository;
import com.example.Tasktracker.service.TaskService;
import com.example.Tasktracker.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DatabaseTaskService implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public Flux<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public Mono<Task> findById(String id) {
        return taskRepository.findById(id);
    }

    @Override
    public Mono<Task> save(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Mono<Task> update(String id, Task task) {
        var taskForUpdate = findById(id).block();

        BeanUtils.copyNonNullProperties(task, taskForUpdate);

        return taskRepository.save(taskForUpdate);
    }

    @Override
    public Mono<Task> setObserver(String taskId, String userId) {
        Task taskForUpdate = findById(taskId).block();
        Set<String> observers = new HashSet<>(taskForUpdate.getObserverIds());
        observers.add(userId);
        taskForUpdate.setObserverIds(observers);

        return taskRepository.save(taskForUpdate);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return taskRepository.deleteById(id);
    }
}
