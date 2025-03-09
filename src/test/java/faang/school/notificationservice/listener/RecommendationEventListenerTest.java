package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.RecommendationRequestedEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Locale;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecommendationEventListenerTest {

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private MessageBuilder<RecommendationRequestedEvent> messageBuilder;

    @Mock
    private NotificationService notificationService;

    private RecommendationRequestEventListener listener;

    @BeforeEach
    void setUp() {
        List<MessageBuilder<RecommendationRequestedEvent>> messageBuilders = List.of(messageBuilder);
        List<NotificationService> notificationServices = List.of(notificationService);
        listener = new RecommendationRequestEventListener(messageBuilders, notificationServices, userServiceClient);
    }

    @Test
    void testOnMessage() {
        RecommendationRequestedEvent event = RecommendationRequestedEvent.builder()
                .requestAuthorId(111L)
                .targetUserId(222L)
                .recommendationRequestId(333L)
                .build();

        when(messageBuilder.getInstance()).thenReturn((Class) RecommendationRequestedEvent.class);
        when(messageBuilder.buildMessage(event, Locale.UK))
                .thenReturn("User 111 requested recommendation, request id 333");

        UserDto userDto = UserDto.builder()
                .id(222L)
                .username("user222")
                .email("user222@example.com")
                .phone("5555555555")
                .preference(UserDto.PreferredContact.EMAIL)
                .build();
        when(userServiceClient.getUser(222L)).thenReturn(userDto);
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        listener.onMessage(event);

        verify(messageBuilder).getInstance();
        verify(messageBuilder).buildMessage(event, Locale.UK);
        verify(userServiceClient).getUser(222L);
        verify(notificationService).getPreferredContact();
        verify(notificationService).send(userDto, "User 111 requested recommendation, request id 333");
    }
}