package faang.school.notificationservice.exception.listener;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.RecommendationRequestedEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.UserDto.PreferredContact;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.redis.connection.Message;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RecommendationRequestedEventListenerTest {

  private static final String SERIALIZED_JSON = "serialized json";
  private static final String MESSAGE_FOR_NOTIFICATION = "message";

  @Mock
  private ObjectMapper objectMapper;

  @Mock
  private UserServiceClient userServiceClient;

  @Mock
  private MessageBuilder<RecommendationRequestedEvent> messageBuilder;

  @Mock
  private NotificationService notificationService;

  @Mock
  private Message redisMessage;

  @InjectMocks
  private RecommendationRequestedEventListener recommendationRequestedEventListener;

  private RecommendationRequestedEvent recommendationRequestedEvent;

  @BeforeEach
  void setUp() {
    recommendationRequestedEvent = RecommendationRequestedEvent.builder()
        .message(UUID.randomUUID().toString())
        .requesterId(1L)
        .receiverId(1L)
        .localDateTime(LocalDateTime.now())
        .build();

    recommendationRequestedEventListener = new RecommendationRequestedEventListener(
        objectMapper,
        userServiceClient,
        List.of(messageBuilder),
        List.of(notificationService)
    );
  }

  @Test
  @DisplayName("Проверка чтения сообщения из топика redis и отправка этого сообщения")
  void testOnReadMessageTopicFromRedisAndSendThisMessage() throws IOException {
    when(redisMessage.getBody()).thenReturn(SERIALIZED_JSON.getBytes());
    when(objectMapper.readValue(SERIALIZED_JSON.getBytes(), RecommendationRequestedEvent.class))
        .thenReturn(recommendationRequestedEvent);
    when(messageBuilder.getInstance()).thenReturn(RecommendationRequestedEvent.class);
    when(messageBuilder.buildMessage(recommendationRequestedEvent, Locale.ROOT))
        .thenReturn(MESSAGE_FOR_NOTIFICATION);
    when(userServiceClient.getUser(1L)).thenReturn(getUserDto());
    when(notificationService.getPreferredContact()).thenReturn(PreferredContact.EMAIL);

    recommendationRequestedEventListener.onMessage(redisMessage, new byte[0]);

    assertDoesNotThrow(() -> recommendationRequestedEventListener.onMessage(redisMessage, new byte[0]));
  }

  @Test
  @DisplayName("Проверка выброса исключения при чтении json")
  void testExceptionOnReadValueFromBodyRedisMessage() throws IOException {
    when(redisMessage.getBody()).thenReturn(SERIALIZED_JSON.getBytes());
    when(objectMapper.readValue(SERIALIZED_JSON.getBytes(), RecommendationRequestedEvent.class))
        .thenThrow(new IOException("Invalid JSON"));

    assertThrows(EventHandlingException.class,
        () -> recommendationRequestedEventListener.onMessage(redisMessage, new byte[0]));
  }

  private UserDto getUserDto() {
    return UserDto.builder()
        .id(1L)
        .username("name")
        .email("email")
        .phone("phone")
        .preference(PreferredContact.EMAIL)
        .build();
  }

}