package com.example.Tasktracker.service.impl;

import com.example.Tasktracker.model.Task;
import com.example.Tasktracker.repository.TaskRepository;
import com.example.Tasktracker.service.TaskService;
import com.example.Tasktracker.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DatabaseTaskService implements TaskService {

    private final DatabaseUserService userService;

    private final TaskRepository taskRepository;

    @Override
    public Flux<Task> findAll() {

        return taskRepository.findAll().flatMap(task -> taskMapper(Mono.just(task)));
    }

    @Override
    public Mono<Task> findById(String id) {

        return taskMapper(taskRepository.findById(id));
    }

    public Mono<Task> taskMapper (Mono<Task> taskMono){

        var observers = taskMono.map(Task::getObserverIds)
                .flatMapMany(Flux::fromIterable)
                .flatMap(userService::findById)
                .collectList().defaultIfEmpty(Collections.emptyList());

        return taskMono.zipWhen(task -> userService.findById(task.getAuthorId()), (t1, t2) -> {
                    t1.setAuthor(t2);
                    return t1;
                })
                .zipWhen(task -> userService.findById(task.getAssigneeId()), (t1, t2) -> {
                    t1.setAssignee(t2);
                    return t1;
                })
                .zipWhen(task -> observers, (t1, t2) -> {
                    t1.setObservers(new HashSet<>(t2));
                    return t1;
                });
    }

    @Override
    public Mono<Task> save(Task task) {
        if (task.getCreatedAt() == null) {
            task.setCreatedAt(Instant.now());
        }
        var savedTask = taskRepository.save(task);
        return taskMapper(savedTask);
    }

    @Override
    public Mono<Task> update(String id, Task task) {

        return Mono.zip(Mono.just(task), findById(id)).flatMap(data -> {
            BeanUtils.copyNonNullProperties(data.getT1(), data.getT2());
            data.getT2().setUpdatedAt(Instant.now());
            return taskRepository.save(data.getT2());
        });
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return taskRepository.deleteById(id);
    }

    @Override
    public Mono<Task> setObserver(String taskId, String userId) {

        return Mono.from(findById(taskId)).flatMap(data -> {
            Set<String> observers = new HashSet<>(data.getObserverIds());
            observers.add(userId);
            data.setObserverIds(observers);
            return taskRepository.save(data);
        });
    }

    @Override
    public Mono<Task> deleteObservers(String taskId) {

        return Mono.from(findById(taskId)).flatMap(data -> {
            data.setObserverIds(Collections.emptySet());
            return taskRepository.save(data);
        });
    }

}
