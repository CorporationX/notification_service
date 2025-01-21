package faang.school.notificationservice.listener.recommendation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.recommendation.RecommendationRequestedEvent;
import faang.school.notificationservice.service.mail.EmailService;
import faang.school.notificationservice.service.telegram.TelegramBot;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@Testcontainers
class RecommendationRequestedEventListenerIT {
    @Container
    private static final GenericContainer<?> REDIS_CONTAINER =
            new GenericContainer<>("redis:latest")
                    .withExposedPorts(6379);

    @DynamicPropertySource
    private static void registerDynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port", REDIS_CONTAINER::getFirstMappedPort);
    }

    @Value("${spring.data.redis.channel.recommendation-requested}")
    private String channel;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @MockBean
    private EmailService emailService;
    @MockBean
    private UserServiceClient userServiceClient;
    @MockBean
    @Qualifier("createBot")
    private TelegramBot telegramBot;

    @Test
    public void shouldSendEmailWhenEventReceived() throws JsonProcessingException, InterruptedException {
        long userId = 1L;
        UserDto userDto = UserDto.builder().preference(UserDto.PreferredContact.EMAIL).build();
        when(userServiceClient.getUser(userId)).thenReturn(userDto);
        when(emailService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        CountDownLatch countDownLatch = new CountDownLatch(1);
        doAnswer(invocationOnMock -> {
            countDownLatch.countDown();
            return null;
        }).when(emailService).send(eq(userDto), anyString());

        RecommendationRequestedEvent requestedEvent = RecommendationRequestedEvent.builder()
                .receiverId(userId)
                .build();
        String json = objectMapper.writeValueAsString(requestedEvent);
        redisTemplate.convertAndSend(channel, json);
        if (!countDownLatch.await(5L, TimeUnit.SECONDS)) {
            fail("Message processing timed out.");
        }

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(emailService).send(eq(userDto), messageCaptor.capture());
        assertEquals("You have a new request for a recommendation.", messageCaptor.getValue());
    }
}