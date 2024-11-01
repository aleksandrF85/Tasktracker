package com.example.Tasktracker.controller;

import com.example.Tasktracker.AbstractTest;
import com.example.Tasktracker.dto.request.UpsertTaskRequest;
import com.example.Tasktracker.dto.response.TaskResponse;
import com.example.Tasktracker.dto.response.UserResponse;
import com.example.Tasktracker.model.TaskStatus;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TaskControllerTest extends AbstractTest {
    @Test
    public void whenGetAllTasks_ThenReturnListOfTasksFromDatabase() {
        var expectedData = List.of(
                new TaskResponse(FIRST_TASK_ID, "First task", "Do first task", FIRST_TASK_CREATED, null, TaskStatus.TODO
                        , new UserResponse(FIRST_USER_ID, "Mr First", "firstmailbox@example.com")
                        , new UserResponse(SECOND_USER_ID, "Mr Second", "secondmailbox@example.com")
                        , (Set.of(new UserResponse(THIRD_USER_ID, "Mr Third", "thirdmailbox@example.com"),
                        new UserResponse(FOURTH_USER_ID, "Mr Fourth", "fourthmailbox@example.com")))),
                new TaskResponse(SECOND_TASK_ID, "Second task", "Do second task", SECOND_TASK_CREATED, null, TaskStatus.TODO
                        , new UserResponse(THIRD_USER_ID, "Mr Third", "thirdmailbox@example.com")
                        , new UserResponse(FOURTH_USER_ID, "Mr Fourth", "fourthmailbox@example.com")
                        , (Set.of(new UserResponse(FIRST_USER_ID, "Mr First", "firstmailbox@example.com"),
                        new UserResponse(SECOND_USER_ID, "Mr Second", "secondmailbox@example.com"))))

        );

        webTestClient.get().uri("/api/task")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TaskResponse.class)
                .hasSize(2)
                .contains(expectedData.toArray(TaskResponse[]::new));

    }

    @Test
    public void whenGetTaskById_ThenReturnTaskFromDatabaseById() {
        var expectedData = new TaskResponse(FIRST_TASK_ID, "First task", "Do first task", FIRST_TASK_CREATED, null, TaskStatus.TODO
                , new UserResponse(FIRST_USER_ID, "Mr First", "firstmailbox@example.com")
                , new UserResponse(SECOND_USER_ID, "Mr Second", "secondmailbox@example.com")
                , (Set.of(new UserResponse(THIRD_USER_ID, "Mr Third", "thirdmailbox@example.com"),
                new UserResponse(FOURTH_USER_ID, "Mr Fourth", "fourthmailbox@example.com"))));

        webTestClient.get().uri("/api/task/{id}", FIRST_TASK_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskResponse.class)
                .isEqualTo(expectedData);
    }

    @Test
    public void whenCreateTask_thenReturnTask() {
        StepVerifier.create(taskRepository.count())
                .expectNext(2L)
                .expectComplete()
                .verify();

        UpsertTaskRequest request = new UpsertTaskRequest();
        request.setName("Test task");
        request.setDescription("create tests");
        request.setAuthorId(SECOND_USER_ID);
        request.setAssigneeId(FOURTH_USER_ID);
        request.setStatus(TaskStatus.TODO);


        webTestClient.post().uri("/api/task")
                .body(Mono.just(request), UpsertTaskRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskResponse.class)
                .value(response -> {
                    assertNotNull(response.getId());
                    assertNotNull(response.getCreatedAt());
                    assertEquals("Test task", response.getName());
                    assertEquals("create tests", response.getDescription());
                    assertEquals(new UserResponse(SECOND_USER_ID, "Mr Second", "secondmailbox@example.com"), response.getAuthor());
                    assertEquals(new UserResponse(FOURTH_USER_ID, "Mr Fourth", "fourthmailbox@example.com"), response.getAssignee());
                    assertEquals(TaskStatus.TODO, response.getStatus());
                });

        StepVerifier.create(taskRepository.count())
                .expectNext(3L)
                .expectComplete()
                .verify();
    }

    @Test
    public void whenUpdateTask_thenReturnUpdatedTask() {
        UpsertTaskRequest request = new UpsertTaskRequest();
        request.setStatus(TaskStatus.IN_PROGRESS);


        webTestClient.put().uri("/api/task/{id}", FIRST_TASK_ID)
                .body(Mono.just(request), UpsertTaskRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskResponse.class)
                .value(response -> {
                    assertNotNull(response.getId());
                    assertNotNull(response.getUpdatedAt());
                    assertEquals(TaskStatus.IN_PROGRESS, response.getStatus());
                    assertEquals("Do first task", response.getDescription());
                });
    }

    @Test
    public void whenDeleteById_thenRemoveTaskFromDatabase() {
        webTestClient.delete().uri("/api/task/{id}", SECOND_TASK_ID)
                .exchange()
                .expectStatus().isNoContent();

        StepVerifier.create(taskRepository.count())
                .expectNext(1L)
                .expectComplete()
                .verify();
    }
}
