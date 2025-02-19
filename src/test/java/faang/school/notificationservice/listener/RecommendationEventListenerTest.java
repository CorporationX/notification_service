package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.context.UserContext;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.RecommendationEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.messaging.RecommendationEventMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.connection.Message;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RecommendationEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private MessageSource messageSource;

    @Mock
    private NotificationService notificationService;

    @Mock
    UserContext userContext;

    @InjectMocks
    private RecommendationEventListener recommendationEventListener;

    private RecommendationEvent recommendationEvent;
    private Message message;
    private UserDto userDto;
    private String messageText;

    @BeforeEach
    void setUp() {
        List<MessageBuilder<RecommendationEvent>> builders = new ArrayList<>();
        builders.add(new RecommendationEventMessageBuilder(messageSource, userServiceClient, userContext));

        List<NotificationService> notificationServices = new ArrayList<>();
        notificationServices.add(notificationService);

        recommendationEventListener = new RecommendationEventListener(
                builders,
                objectMapper,
                userServiceClient,
                notificationServices,
                userContext);

        recommendationEvent = new RecommendationEvent(1L, 2L, 3L);
        message = mock(Message.class);
        userDto = new UserDto();
        userDto.setId(2L);
        userDto.setUsername("testUser");
        userDto.setPreference(UserDto.PreferredContact.TELEGRAM);
        messageText = "You have received a recommendation request from a user testUser";
    }

    @Test
    void testOnMessage_Success() throws IOException {
        byte[] pattern = new byte[0];
        when(message.getBody()).thenReturn("{\"requesterId\":1,\"receiverId\":2,\"recommendationId\":3}".getBytes());
        when(objectMapper.readValue(any(byte[].class), eq(RecommendationEvent.class))).thenReturn(recommendationEvent);
        when(userServiceClient.getUser(anyLong())).thenReturn(userDto);
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.TELEGRAM);
        when(messageSource.getMessage(
                eq("recommendation.new"),
                eq(new Object[]{userDto.getUsername()}),
                eq(Locale.ENGLISH))
        ).thenReturn(messageText);

        recommendationEventListener.onMessage(message, pattern);

        verify(objectMapper, times(1)).readValue(any(byte[].class),
                eq(RecommendationEvent.class));
        verify(userServiceClient, times(1)).getUser(1L);
        verify(notificationService, times(1)).send(userDto, messageText);
    }

    @Test
    void testOnMessage_IOException() throws IOException {
        when(message.getBody()).thenReturn("{\"requesterId\":1,\"receiverId\":2,\"recommendationId\":3}".getBytes());
        when(objectMapper.readValue(any(byte[].class), eq(RecommendationEvent.class)))
                .thenThrow(new IOException("JSON error"));

        assertThrows(RuntimeException.class, () -> recommendationEventListener.onMessage(message, null));

        verify(objectMapper, times(1)).readValue(any(byte[].class),
                eq(RecommendationEvent.class));
        verify(userServiceClient, never()).getUser(anyLong());
        verify(notificationService, never()).send(any(), anyString());
    }

    @Test
    void testGetMessage_Success() {
        when(userServiceClient.getUser(anyLong())).thenReturn(userDto);
        when(messageSource.getMessage(
                eq("recommendation.new"),
                eq(new Object[]{userDto.getUsername()}),
                eq(Locale.ENGLISH))
        ).thenReturn(messageText);

        String result = recommendationEventListener.getMessage(2L, recommendationEvent);

        assertEquals(messageText, result);
        verify(userServiceClient, times(1)).getUser(1L);
    }

    @Test
    void testSendNotification_Success() {
        when(userServiceClient.getUser(2L)).thenReturn(userDto);
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.TELEGRAM);

        recommendationEventListener.sendNotification(2L, messageText);

        verify(userServiceClient, times(1)).getUser(2L);
        verify(notificationService, times(1)).send(userDto, messageText);
    }
}
