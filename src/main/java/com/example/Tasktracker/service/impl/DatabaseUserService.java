package com.example.Tasktracker.service.impl;

import com.example.Tasktracker.model.User;
import com.example.Tasktracker.repository.UserRepository;
import com.example.Tasktracker.service.UserService;
import com.example.Tasktracker.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class DatabaseUserService implements UserService {

    private final UserRepository userRepository;

    @Override
    public Flux<User> findAll() {
        return userRepository.findAll();
    }


    @Override
    public Mono<User> findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public Mono<User> save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Mono<User> update(String id, User user) {

        return Mono.zip(Mono.just(user), findById(id)).flatMap(data -> {
            BeanUtils.copyNonNullProperties(data.getT1(), data.getT2());
            return userRepository.save(data.getT2());
        });
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return userRepository.deleteById(id);
    }
}
