package faang.school.notificationservice.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.GoalCompletedEventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.GoalCompletedMessageBuilder;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.connection.Message;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GoalCompletedEventListenerTest {
    @InjectMocks
    private GoalCompletedEventListener goalCompletedEventListener;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private NotificationService mockNotificationService;

    @Mock
    private Message redisMessage;

    @Mock
    private MessageSource messageSource;

    @Mock
    private GoalCompletedMessageBuilder mockMessageBuilder;

    private List<MessageBuilder<GoalCompletedEventDto>> messageBuilders;
    private List<NotificationService> notificationServices;

    @BeforeEach
    void init() {
        mockMessageBuilder = spy(new GoalCompletedMessageBuilder(messageSource));
        notificationServices = List.of(mockNotificationService);
        messageBuilders = new ArrayList<>(List.of(mockMessageBuilder));
        goalCompletedEventListener = new GoalCompletedEventListener(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Test
    public void testOnMessageWhenBodyIsNull() {
        when(redisMessage.getBody()).thenReturn(null);

        goalCompletedEventListener.onMessage(redisMessage, null);

        verifyNoInteractions(userServiceClient, mockNotificationService);
    }

    @Test
    public void testOnMessageWhenInputIsValid() throws Exception {
        GoalCompletedEventDto eventDto = new GoalCompletedEventDto(1L, 1L, LocalDateTime.now());
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("email");
        userDto.setPreference(UserDto.PreferredContact.EMAIL);
        String messageText = "Goal completed message";
        messageBuilders.add(new GoalCompletedMessageBuilder(messageSource));

        String eventJson = "{\"userId\":1,\"goalId\":1}";
        when(redisMessage.getBody()).thenReturn(eventJson.getBytes());
        when(objectMapper.readValue(Mockito.<byte[]>any(), eq(GoalCompletedEventDto.class)))
                .thenReturn(eventDto);
        when(userServiceClient.getUser(eventDto.getUserId())).thenReturn(userDto);
        when(mockMessageBuilder.buildMessage(eventDto, Locale.getDefault())).thenReturn(messageText);
        when(mockNotificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        goalCompletedEventListener.onMessage(redisMessage, null);

        verify(mockNotificationService, times(1)).send(userDto, messageText);
    }
}
