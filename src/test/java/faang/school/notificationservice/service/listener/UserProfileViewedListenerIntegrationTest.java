package faang.school.notificationservice.service.listener;

import faang.school.notificationservice.dto.UserServiceDto;
import faang.school.notificationservice.dto.kafka.UserProfileViewedDto;
import faang.school.notificationservice.exception.impl.non_retryable.DtoValidationFailedException;
import faang.school.notificationservice.handler.KafkaMapperHandler;
import faang.school.notificationservice.handler.MessageHandler;
import faang.school.notificationservice.handler.NotificationServiceHandler;
import faang.school.notificationservice.handler.UserServiceHandler;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.KafkaTemplate;
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
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@SpringBootTest
@Testcontainers
public class UserProfileViewedListenerIntegrationTest {

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
    private KafkaTemplate<String, Object> kafkaTemplate;

    @SpyBean
    private NotificationServiceHandler notificationServiceHandler;

    @MockBean
    private UserServiceHandler userServiceHandler;

    @MockBean
    private KafkaMapperHandler kafkaMapperHandler;

    @MockBean
    private MessageHandler<UserProfileViewedDto> messageHandler;

    private CountDownLatch latch;

    @BeforeAll
    static void createTopics() throws Exception {
        Awaitility.await().atMost(30, TimeUnit.SECONDS).until(KAFKA_CONTAINER::isRunning);

        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_CONTAINER.getBootstrapServers());
        try (AdminClient adminClient = AdminClient.create(props)) {
            adminClient.createTopics(Arrays.asList(
                    new NewTopic("user-profile-viewed", 3, (short) 1),
                    new NewTopic("user-profile-viewed.DLT", 3, (short) 1)
            )).all().get();
        }
    }

    @BeforeEach
    public void setUp() {
        latch = new CountDownLatch(1);

        doAnswer(invocation -> {
            latch.countDown();
            return null;
        }).when(notificationServiceHandler).sendSingleNotification(any(UserServiceDto.class), any(String.class));

        UserServiceDto owner = new UserServiceDto();
        owner.setId(1L);
        owner.setUsername("ownerUser");

        UserServiceDto viewer = new UserServiceDto();
        viewer.setId(2L);
        viewer.setUsername("viewerUser");

        when(userServiceHandler.getUsersByIdsInGivenOrder(any())).thenReturn(Arrays.asList(owner, viewer));

        UserProfileViewedDto testDto = new UserProfileViewedDto(1L, 2L, LocalDateTime.now());
        when(kafkaMapperHandler.mapAndValidateKafkaEvent(any(), eq(UserProfileViewedDto.class)))
                .thenReturn(testDto);

        when(messageHandler.getMessage(any(), any(), any())).thenReturn("Test notification message");
    }

    static Stream<Arguments> provideTestScenarios() {
        UserProfileViewedDto dto = new UserProfileViewedDto(1L, 2L, LocalDateTime.now());
        return Stream.of(
                org.junit.jupiter.params.provider.Arguments.of("valid", dto),
                org.junit.jupiter.params.provider.Arguments.of("duplicate", dto),
                org.junit.jupiter.params.provider.Arguments.of("invalid", dto)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestScenarios")
    void testMessageScenarios(String scenario, UserProfileViewedDto dto) throws Exception {
        switch (scenario) {
            case "valid":
                UUID key = UUID.randomUUID();
                latch = new CountDownLatch(1);
                kafkaTemplate.send("user-profile-viewed", key.toString(), dto);
                boolean processed = latch.await(10, TimeUnit.SECONDS);
                assertTrue(processed);

                break;

            case "duplicate":
                UUID fixedKey = UUID.randomUUID();

                kafkaTemplate.send("user-profile-viewed", fixedKey.toString(), dto);
                boolean firstProcessed = latch.await(10, TimeUnit.SECONDS);
                assertTrue(firstProcessed);

                kafkaTemplate.send("user-profile-viewed", fixedKey.toString(), dto);

                ConsumerRecord<String, Object> dlqRecord = Awaitility.await()
                        .atMost(10, TimeUnit.SECONDS)
                        .pollInterval(1, TimeUnit.SECONDS)
                        .until(() -> pollDLQ("user-profile-viewed.DLT"),
                                Objects::nonNull);

                assertNotNull(dlqRecord);
                assertTrue(dlqRecord.key().contains(fixedKey.toString()));
                break;

            case "invalid":
                UUID secondKey = UUID.randomUUID();
                when(kafkaMapperHandler.mapAndValidateKafkaEvent(any(), eq(UserProfileViewedDto.class)))
                        .thenThrow(new DtoValidationFailedException("Incorrect message"));
                kafkaTemplate.send("user-profile-viewed", secondKey.toString(), dto);

                ConsumerRecord<String, Object> dlqSecondRecord = Awaitility.await()
                        .atMost(10, TimeUnit.SECONDS)
                        .pollInterval(1, TimeUnit.SECONDS)
                        .until(() -> pollDLQ("user-profile-viewed.DLT"),
                                Objects::nonNull);

                assertTrue(dlqSecondRecord.key().contains(secondKey.toString()));
                break;
            default:
                fail("Unknown " + scenario);
        }
    }

    private ConsumerRecord<String, Object> pollDLQ(String dlqTopic) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_CONTAINER.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-dlq-consumer");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        try (KafkaConsumer<String, Object> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Arrays.asList(dlqTopic));
            ConsumerRecords<String, Object> records = consumer.poll(Duration.ofSeconds(10));
            if (records.count() > 0) {
                return records.iterator().next();
            }
        }
        return null;
    }
}