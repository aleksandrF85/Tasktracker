package com.example.Tasktracker.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpsertUserRequest {

    @NotBlank(message = "Имя должно быть указано")
    @Size(min = 5, max = 20, message = "Имя не должно быть меньше {min} и больше {max} символов!")
    String username;

    //    @Email(message = "Email не верно указан", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @NotEmpty(message = "Email должен быть указан")
    String email;
}
