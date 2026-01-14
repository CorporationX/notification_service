package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.RedisConfig;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.FollowerEvent;
import faang.school.notificationservice.messaging.FollowerMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        RedisConfig.class,
        FollowerEventListener.class,
        FollowerMessageBuilder.class,
        MessageSourceAutoConfiguration.class,
        JacksonAutoConfiguration.class
})
@Testcontainers
public class FollowerEventListenerIntegrationTest {

    @Container
    static GenericContainer<?> redisContainer =
            new GenericContainer<>("redis:7.2")
                    .withExposedPorts(6379);

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.port",
                () -> redisContainer.getMappedPort(6379));
        registry.add("spring.data.redis.host",
                () -> redisContainer.getHost());
    }

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ChannelTopic followerTopic;

    @MockBean
    private UserServiceClient userServiceClient;

    @Autowired
    private FollowerMessageBuilder messageBuilder;

    @MockBean
    private NotificationService notificationService;

    @Test
    void shouldConsumeEventAndSendNotification() {
        when(notificationService.getPreferredContact())
                .thenReturn(UserDto.PreferredContact.EMAIL);

        FollowerEvent event = new FollowerEvent(1L, 2L, LocalDateTime.now());

        UserDto notificationReceiver = new UserDto();
        notificationReceiver.setId(1L);

        when(userServiceClient.getUser(1L))
                .thenReturn(notificationReceiver);

        UserDto follower = new UserDto();
        follower.setUsername("Cristiano Ronaldo");

        when(userServiceClient.getUser(2L))
                .thenReturn(follower);

        String builtMessage = messageBuilder.buildMessage(event, Locale.ENGLISH);

        redisTemplate.convertAndSend(followerTopic.getTopic(), event);

        Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        verify(notificationService)
                                .send(notificationReceiver, builtMessage)
                );
    }
}
