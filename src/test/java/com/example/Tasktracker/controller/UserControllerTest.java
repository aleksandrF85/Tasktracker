package com.example.Tasktracker.controller;

import com.example.Tasktracker.AbstractTest;
import com.example.Tasktracker.dto.response.UserResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

public class UserControllerTest extends AbstractTest {

    @Test
    public void whenGetAllUsers_ThenReturnListOfUsersFromDatabase() {
        var expectedData = List.of(
                new UserResponse(FIRST_USER_ID, "Mr First", "firstmailbox@example.com"),
                new UserResponse(SECOND_USER_ID, "Mr Second", "secondmailbox@example.com"),
                new UserResponse(THIRD_USER_ID, "Mr Third", "thirdmailbox@example.com"),
                new UserResponse(FOURTH_USER_ID, "Mr Fourth", "fourthmailbox@example.com")
        );

        webTestClient.get().uri("/api/user")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserResponse.class)
                .hasSize(4)
                .contains(expectedData.toArray(UserResponse[]::new));

    }
}
