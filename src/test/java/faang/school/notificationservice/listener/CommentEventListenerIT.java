package faang.school.notificationservice.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.kafkaevents.CommentEvent;
import faang.school.notificationservice.model.NotificationType;
import faang.school.notificationservice.repository.NotificationEventLogRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class CommentEventListenerIT {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NotificationEventLogRepository repository;

    @Value("${spring.data.kafka.topic.comment}")
    private String topic;

    @Container
    private static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>("postgres:13.6");

    @Container
    private static final KafkaContainer KAFKA_CONTAINER = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.0"));

    @DynamicPropertySource
    static void start(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRE_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRE_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRE_SQL_CONTAINER::getPassword);

        registry.add("spring.data.kafka.bootstrap-servers", KAFKA_CONTAINER::getBootstrapServers);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException("Thread sleep interrupted", e);
        }
    }

    @Test
    void testCommentEventListener() throws JsonProcessingException {
        CommentEvent commentEvent = createCommentEvent();
        kafkaTemplate.send(topic, objectMapper.writeValueAsString(commentEvent));

        Awaitility.await()
                .atMost(Duration.ofMillis(2000))
                .untilAsserted(() ->
                        assertTrue(repository.checkExistingEvent(commentEvent.id(), NotificationType.COMMENT))
                );
    }

    private CommentEvent createCommentEvent() {
        return CommentEvent.builder()
                .id(10)
                .commentAuthorId(1)
                .postAuthorId(2)
                .postId(2)
                .content("testContainer3")
                .createdAt(LocalDateTime.now())
                .build();
    }
}
