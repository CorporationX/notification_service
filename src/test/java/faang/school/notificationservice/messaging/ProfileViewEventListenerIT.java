package faang.school.notificationservice.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.context.RedisConfig;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.profile.ProfileViewEvent;
import faang.school.notificationservice.listener.ProfileViewEventListener;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.telegram.TelegramService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@Testcontainers
@Import(RedisConfig.class)
@EnableAutoConfiguration(exclude = WebMvcAutoConfiguration.class)
public class ProfileViewEventListenerIT {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserServiceClient userServiceClient;

    @MockBean
    private MessageBuilderProfileViewEvent messageBuilderProfileViewEvent;

    @MockBean
    private TelegramService telegramService;

    @MockBean
    private List<NotificationService> notificationServices;

    @MockBean
    private Message message;

    @Container
    public static PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:13.6");

    @Container
    public static final GenericContainer<?> REDIS_CONTAINER =
            new GenericContainer<>(DockerImageName.parse("redis/redis-stack:latest"))
                    .withExposedPorts(6379);

    @DynamicPropertySource
    static void start(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);
        registry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379));
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);

        try {
            Thread.sleep(11000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test() throws IOException, InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        ProfileViewEvent profileViewEvent = ProfileViewEvent.builder()
                .authorId(1L)
                .viewerId(2L)
                .localDateTime(LocalDateTime.now())
                .build();
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        when(userServiceClient.getUser(1L)).thenReturn(userDto);

        ProfileViewEventListener listener = new ProfileViewEventListener(
                objectMapper,
                Collections.singletonList(messageBuilderProfileViewEvent),
                userServiceClient,
                notificationServices
        ) {
            @Override
            public void onMessage(Message message, byte[] pattern) {
                try {
                    ProfileViewEvent event = objectMapper.readValue(message.getBody(), ProfileViewEvent.class);
                    UserDto user = userServiceClient.getUser(event.getAuthorId());
                    telegramService.send(user, "Some notification message");
                    latch.countDown();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(() -> {
            redisTemplate.getConnectionFactory().getConnection().subscribe(listener, "profile_view_channel".getBytes());
        }).start();
        String eventJson = objectMapper.writeValueAsString(profileViewEvent);
        redisTemplate.convertAndSend("profile_view_channel", eventJson);
        boolean completed = latch.await(15, TimeUnit.SECONDS);
        assertTrue(completed, "Test timed out waiting for message to be processed");
        verify(telegramService).send(any(UserDto.class),anyString());
        verify(userServiceClient).getUser(1L);
    }
}