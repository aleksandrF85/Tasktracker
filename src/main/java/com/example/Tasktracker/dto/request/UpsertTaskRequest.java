package com.example.Tasktracker.dto.request;

import com.example.Tasktracker.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpsertTaskRequest {

    @NotBlank(message = "Название должно быть указано")
    String name;
    @NotBlank(message = "Описание должно быть указано")
    String description;

    TaskStatus status;

    String authorId;

    String assigneeId;

}
