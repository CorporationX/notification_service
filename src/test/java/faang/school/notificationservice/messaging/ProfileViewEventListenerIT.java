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
import java.util.List;
import java.util.Locale;
import static faang.school.notificationservice.dto.UserDto.PreferredContact.TELEGRAM;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
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
    @Autowired
    private ProfileViewEventListener listener;

    @MockBean
    private UserServiceClient userServiceClient;

    @MockBean
    private List<MessageBuilder<ProfileViewEvent>> messageBuilders;

    @MockBean
    private TelegramService telegramService;

    @MockBean
    private List<NotificationService> notificationServices;

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

    }

    @Test
    public void test() throws IOException, InterruptedException {
        MessageBuilder messageBuilder = mock(MessageBuilder.class);
        when(messageBuilder.getInstance()).thenReturn(ProfileViewEvent.class);
        messageBuilders = List.of(messageBuilder);
        NotificationService notificationService = mock(NotificationService.class);
        when(notificationService.getPreferredContact()).thenReturn(TELEGRAM);
        notificationServices = List.of(notificationService);
        ProfileViewEvent profileViewEvent = ProfileViewEvent.builder()
                .authorId(1L)
                .viewerName("name")
                .localDateTime(LocalDateTime.now())
                .build();
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("123");
        userDto.setPreference(TELEGRAM);
        when(userServiceClient.getUser(1L)).thenReturn(userDto);

        when(messageBuilders.get(0).buildMessage(profileViewEvent, Locale.UK)).thenReturn("iii");
        when(telegramService.getPreferredContact()).thenReturn(TELEGRAM);
        String eventJson = objectMapper.writeValueAsString(profileViewEvent);
        redisTemplate.convertAndSend("profile_view_channel", eventJson);
        Thread.sleep(15000);
        verify(userServiceClient, times(1)).getUser(1L);
        verify(telegramService).send(any(UserDto.class), anyString());
    }
}