package com.example.Tasktracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @Field("observers")
    Set<String> observerIds;

    /*Также у сущности должны быть поля, не попадающие в базу данных:*/
    @Transient
    User author;

    @Transient
    User assignee;

    @Transient
    Set<User> observers;
}
