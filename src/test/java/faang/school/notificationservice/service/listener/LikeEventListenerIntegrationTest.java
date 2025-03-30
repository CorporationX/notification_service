package faang.school.notificationservice.service.listener;

import faang.school.notificationservice.dto.LikeEvent;
import faang.school.notificationservice.dto.UserServiceDto;
import faang.school.notificationservice.entity.Event;
import faang.school.notificationservice.handler.NotificationServiceHandler;
import faang.school.notificationservice.handler.UserServiceHandler;
import faang.school.notificationservice.repository.EventRepository;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@Testcontainers
public class LikeEventListenerIntegrationTest {

    @Container
    public static final KafkaContainer KAFKA_CONTAINER =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.3.1"));

    @Container
    private static final PostgreSQLContainer<?> POSTGRES_CONTAINER =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:13.3"))
                    .withDatabaseName("postgres")
                    .withUsername("user")
                    .withPassword("password");

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRES_CONTAINER::getPassword);
        registry.add("spring.kafka.consumer.bootstrap-servers", KAFKA_CONTAINER::getBootstrapServers);
        registry.add("spring.kafka.producer.bootstrap-servers", KAFKA_CONTAINER::getBootstrapServers);
    }


    @Autowired
    private LikeEventListener likeEventListener;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private EventRepository eventRepository;

    @MockBean
    private UserServiceHandler userServiceHandler;

    @MockBean
    private NotificationServiceHandler notificationServiceHandler;

    UserServiceDto authorDto = new UserServiceDto();
    LikeEvent likeEventDto = new LikeEvent();

    @BeforeAll
    static void createTopics() throws Exception {
        Awaitility.await().atMost(30, TimeUnit.SECONDS).until(KAFKA_CONTAINER::isRunning);

        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_CONTAINER.getBootstrapServers());
        try (AdminClient adminClient = AdminClient.create(props)) {
            adminClient.createTopics(Arrays.asList(
                    new NewTopic("user-like-post", 5, (short) 1),
                    new NewTopic("user-like-post.DLT", 1, (short) 1)
            )).all().get();
        }
    }

    @BeforeEach
    public void setUp() {

        authorDto = UserServiceDto.builder()
                .id(1L)
                .username("author")
                .preference(UserServiceDto.PreferredContact.TELEGRAM)
                .build();

        UserServiceDto likerDto = UserServiceDto.builder()
                .id(2L)
                .username("liker")
                .preference(UserServiceDto.PreferredContact.TELEGRAM)
                .build();

        likeEventDto = LikeEvent.builder()
                .postId(2L)
                .userId(2L)
                .authorId(1L)
                .likeTime(LocalDateTime.now())
                .build();

        when(userServiceHandler.getSingleUser(1L)).thenReturn(authorDto);
        when(userServiceHandler.getSingleUser(2L)).thenReturn(likerDto);
    }

    @Test
    void testListenTopic() {

        ConsumerRecord<String, Object> event = new ConsumerRecord<>(
                "user-like-post", 5, 0L, "dummyKey", likeEventDto);

        likeEventListener.listen(event);

        List<Event> events = eventRepository.findAll();
        assertEquals(1, events.size());

        verify(notificationServiceHandler, times(1)).sendSingleNotification(eq(authorDto),
                eq("\"User liker liked your post!\""));
    }
}