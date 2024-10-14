package com.example.Tasktracker.model;

import lombok.AllArgsConstructor;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Document(collection = "tasks")
public class Task {

    @Id
    String id;

    String name;

    String description;

    Instant createdAt;

    Instant updatedAt;

    TaskStatus status;

    String authorId;

    String assigneeId;

    @Builder.Default
    @Field("observers")
    Set<String> observerIds = new HashSet<>();

    /*Также у сущности должны быть поля, не попадающие в базу данных:*/
    @Transient
    User author;

    @Transient
    User assignee;

    @Builder.Default
    @Transient
    Set<User> observers = new HashSet<>();
}
