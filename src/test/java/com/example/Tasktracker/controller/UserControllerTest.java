package com.example.Tasktracker.controller;

import com.example.Tasktracker.AbstractTest;
import com.example.Tasktracker.dto.request.UpsertUserRequest;
import com.example.Tasktracker.dto.response.UserResponse;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


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

    @Test
    public void whenGetUserById_ThenReturnUserFromDatabaseById() {
        var expectedData = new UserResponse(SECOND_USER_ID, "Mr Second", "secondmailbox@example.com");

        webTestClient.get().uri("/api/user/{id}", SECOND_USER_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponse.class)
                .isEqualTo(expectedData);
    }

    @Test
    public void whenCreateUser_thenReturnUser() {
        StepVerifier.create(userRepository.count())
                .expectNext(4L)
                .expectComplete()
                .verify();

        UpsertUserRequest request = new UpsertUserRequest();
        request.setUsername("Test User");
        request.setEmail("test@example.com");

        webTestClient.post().uri("/api/user")
                .body(Mono.just(request), UpsertUserRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponse.class)
                .value(response -> {
                    assertNotNull(response.getId());
                    assertEquals("Test User", response.getUsername());
                    assertEquals("test@example.com", response.getEmail());
                });

        StepVerifier.create(userRepository.count())
                .expectNext(5L)
                .expectComplete()
                .verify();
    }

    @Test
    public void whenUpdateUser_thenReturnUpdatedUser() {
        UpsertUserRequest request = new UpsertUserRequest();
        request.setUsername("Test User");


        webTestClient.put().uri("/api/user/{id}", FIRST_USER_ID)
                .body(Mono.just(request), UpsertUserRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponse.class)
                .value(response -> {
                    assertNotNull(response.getId());
                    assertEquals("Test User", response.getUsername());
                    assertEquals("firstmailbox@example.com", response.getEmail());
                });
    }

    @Test
    public void whenDeleteById_thenRemoveUserFromDatabase() {
        webTestClient.delete().uri("/api/user/{id}", FIRST_USER_ID)
                .exchange()
                .expectStatus().isNoContent();

        StepVerifier.create(userRepository.count())
                .expectNext(3L)
                .expectComplete()
                .verify();
    }

}
