package faang.school.notificationservice.listener.recommendationReceived;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.recommendationReceived.RecommendationReceivedEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.util.Collections;
import java.util.Locale;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecommendationReceivedListenerTest {
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private MessageBuilder<RecommendationReceivedEvent> messageBuilder;
    @Mock
    private NotificationService notificationService;
    @Mock
    private Message redisMessage;

    @InjectMocks
    private RecommendationReceivedListener recommendationReceivedListener;

    private RecommendationReceivedEvent recommendationReceivedEvent;
    private String jsonString = "{\"authorId\":1L,\"receivedId\":2L,\"recommendationId\":3L}";

    @BeforeEach
    void setUp() {
        recommendationReceivedListener = new RecommendationReceivedListener(
                objectMapper,
                userServiceClient,
                Collections.singletonList(messageBuilder),
                Collections.singletonList(notificationService)
        );
        recommendationReceivedEvent = RecommendationReceivedEvent.builder()
                .authorId(1L)
                .receivedId(2L)
                .recommendationId(3L)
                .build();
    }

    @Test
    void testOnMessage() throws Exception {
        when(redisMessage.getBody()).thenReturn(jsonString.getBytes());
        when(objectMapper.readValue(Mockito.any(byte[].class), Mockito.eq(RecommendationReceivedEvent.class)))
                .thenReturn(recommendationReceivedEvent);

        String notificationContent = "RecommendationReceived test";
        when(messageBuilder.getInstance()).thenReturn(RecommendationReceivedEvent.class);
        when(messageBuilder.buildMessage(recommendationReceivedEvent, Locale.UK)).thenReturn(notificationContent);

        UserDto userDto = new UserDto();
        userDto.setPreference(UserDto.PreferredContact.EMAIL);
        when(userServiceClient.getUser(2L)).thenReturn(userDto);
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        recommendationReceivedListener.onMessage(redisMessage, null);

        verify(redisMessage, times(1)).getBody();
        verify(objectMapper, times(1))
                .readValue(Mockito.any(byte[].class), Mockito.eq(RecommendationReceivedEvent.class));
        verify(messageBuilder, times(1)).getInstance();
        verify(messageBuilder, times(1)).buildMessage(recommendationReceivedEvent, Locale.UK);
    }
}