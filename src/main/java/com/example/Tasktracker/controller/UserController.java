package com.example.Tasktracker.controller;

import com.example.Tasktracker.dto.request.UpsertUserRequest;
import com.example.Tasktracker.dto.response.UserResponse;
import com.example.Tasktracker.mapper.UserMapper;
import com.example.Tasktracker.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    @GetMapping
    public Flux<UserResponse> findAll() {
        return userService.findAll().map(userMapper::userToResponse);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> findById(@PathVariable String id) {
        return userService.findById(id).map(userMapper::userToResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<UserResponse>> create(@RequestBody @Valid UpsertUserRequest request) {
        return userService.save(userMapper.requestToUser(request))
                .map(userMapper::userToResponse)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> update(@PathVariable String id, @RequestBody @Valid UpsertUserRequest request) {
        return userService.update(id, userMapper.requestToUser(id, request))
                .map(userMapper::userToResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable String id) {
        return userService.deleteById(id).then(Mono.just(ResponseEntity.noContent().build()));
    }
}
