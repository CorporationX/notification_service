package faang.school.notificationservice.service.listener;

import faang.school.notificationservice.dto.LikeEvent;
import faang.school.notificationservice.dto.UserServiceDto;
import faang.school.notificationservice.handler.KafkaMapperHandler;
import faang.school.notificationservice.handler.MessageHandler;
import faang.school.notificationservice.handler.NotificationServiceHandler;
import faang.school.notificationservice.handler.UserServiceHandler;
import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
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
import org.testcontainers.utility.DockerImageName;

import java.util.Arrays;
import java.util.Properties;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

    @BeforeAll
    static void startContainer() {
        POSTGRES_CONTAINER.start();
        KAFKA_CONTAINER.start();
        createTopics();
    }

    private static void createTopics() {
        Properties properties = new Properties();
        properties.put(
                AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_CONTAINER.getBootstrapServers()
        );
        Admin admin = Admin.create(properties);
        admin.createTopics(Arrays.asList(
                new NewTopic("user-like-post", 5, (short) 1),
                new NewTopic("user-like-post.DLT", 1, (short) 1)));
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRES_CONTAINER::getPassword);
        registry.add("spring.kafka.consumer.bootstrap-servers", KAFKA_CONTAINER::getBootstrapServers);
        registry.add("spring.kafka.producer.bootstrap-servers", KAFKA_CONTAINER::getBootstrapServers);
    }

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @MockBean
    private NotificationServiceHandler notificationServiceHandler;

    @MockBean
    private UserServiceHandler userServiceHandler;

    @MockBean
    private KafkaMapperHandler kafkaMapperHandler;

    @MockBean
    private MessageHandler<LikeEvent> messageHandler;

    @Autowired
    private LikeEventListener likeEventListener;

    private LikeEvent testEvent;
    private UserServiceDto testPostAuthor;
    private UserServiceDto testLiker;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testEvent = new LikeEvent();
        testEvent.setAuthorId(1L);
        testEvent.setUserId(2L);

        testPostAuthor = new UserServiceDto();
        testPostAuthor.setId(1L);
        testPostAuthor.setUsername("author");

        testLiker = new UserServiceDto();
        testLiker.setId(2L);
        testLiker.setUsername("liker");

        when(userServiceHandler.getSingleUser(1L)).thenReturn(testPostAuthor);
        when(userServiceHandler.getSingleUser(2L)).thenReturn(testLiker);
        when(kafkaMapperHandler.mapAndValidateKafkaEvent(any(), eq(LikeEvent.class))).thenReturn(testEvent);
    }

    @Test
    void testListen() {

        kafkaTemplate.send("user-like-post", testEvent);




//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//        UserServiceDto owner = new UserServiceDto();
//        owner.setId(1L);
//        owner.setUsername("ownerUser");
//
//        UserServiceDto liker = new UserServiceDto();
//        liker.setId(2L);
//        liker.setUsername("likerUser");
//
//        when(userServiceHandler.getSingleUser(owner.getId())).thenReturn(owner);
//        when(userServiceHandler.getSingleUser(liker.getId())).thenReturn(liker);
//
//
//        LikeEvent testDto = new LikeEvent(2L, 1L, LocalDateTime.now(), 2L);
//        when(kafkaMapperHandler.mapAndValidateKafkaEvent(any(), eq(LikeEvent.class)))
//                .thenReturn(testDto);
//
//        @Test
//        public void testListen() {
//
//            ConsumerRecord<String, Object> record = new ConsumerRecord<>("user-like-post", 5, 1L,
//                    "key", testDto);
//
//            likeEventListener.listen(record);
//
////        when(messageHandler.getMessage(any(), any(), any())).thenReturn("Test notification message");
//
//    }
    }
}



