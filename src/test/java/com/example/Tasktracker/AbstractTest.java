package com.example.Tasktracker;

import com.example.Tasktracker.model.Task;
import com.example.Tasktracker.model.TaskStatus;
import com.example.Tasktracker.model.User;
import com.example.Tasktracker.repository.TaskRepository;
import com.example.Tasktracker.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@SpringBootTest
@Testcontainers
@AutoConfigureWebTestClient
public class AbstractTest {

    protected static String FIRST_USER_ID = UUID.randomUUID().toString();

    protected static String SECOND_USER_ID = UUID.randomUUID().toString();

    protected static String THIRD_USER_ID = UUID.randomUUID().toString();

    protected static String FOURTH_USER_ID = UUID.randomUUID().toString();

    protected static String FIRST_TASK_ID = UUID.randomUUID().toString();

    protected static String SECOND_TASK_ID = UUID.randomUUID().toString();

    protected static Instant FIRST_TASK_CREATED = Instant.now().minusMillis(1000);

    protected static Instant SECOND_TASK_CREATED = Instant.now();


    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest")
            .withReuse(true);

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.data.mongodb.host", () -> URI.create(mongoDBContainer.getReplicaSetUrl()).getHost());
//        registry.add("spring.data.mongodb.port", () -> URI.create(mongoDBContainer.getReplicaSetUrl()).getPort());
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);

    }

    @Autowired
    protected WebTestClient webTestClient;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    TaskRepository taskRepository;

    @BeforeEach
    public void setUp() {

        userRepository.saveAll(
                List.of(
                        new User(FIRST_USER_ID, "Mr First", "firstmailbox@example.com"),
                        new User(SECOND_USER_ID, "Mr Second", "secondmailbox@example.com"),
                        new User(THIRD_USER_ID, "Mr Third", "thirdmailbox@example.com"),
                        new User(FOURTH_USER_ID, "Mr Fourth", "fourthmailbox@example.com")
                )
        ).collectList().block();

        taskRepository.saveAll(
                List.of(
                        Task.builder().id(FIRST_TASK_ID)
                                .name("First task")
                                .description("Do first task")
                                .createdAt(FIRST_TASK_CREATED)
                                .status(TaskStatus.TODO)
                                .authorId(FIRST_USER_ID)
                                .assigneeId(SECOND_USER_ID)
                                .observerIds(Set.of(THIRD_USER_ID, FOURTH_USER_ID)).build(),
                        Task.builder().id(SECOND_TASK_ID)
                                .name("Second task")
                                .description("Do second task")
                                .createdAt(SECOND_TASK_CREATED)
                                .status(TaskStatus.TODO)
                                .authorId(THIRD_USER_ID)
                                .assigneeId(FOURTH_USER_ID)
                                .observerIds(Set.of(FIRST_USER_ID, SECOND_USER_ID)).build()
                )
        ).collectList().block();
    }

    @AfterEach
    public void afterEach() {
        taskRepository.deleteAll().block();
        userRepository.deleteAll().block();
    }
}
