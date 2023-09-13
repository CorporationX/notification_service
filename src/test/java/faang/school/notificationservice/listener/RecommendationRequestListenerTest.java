package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.EventRecommendationRequestDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.entity.PreferredContact;
import faang.school.notificationservice.exception.NotFoundException;
import faang.school.notificationservice.message.MessageBuilder;
import faang.school.notificationservice.message.RecommendationRequestMessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import faang.school.notificationservice.service.notification.TelegramService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class RecommendationRequestListenerTest {
    @Spy
    private ObjectMapper objectMapper;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private TelegramService telegramService;
    @Mock
    private RecommendationRequestMessageBuilder messageBuilder;
    @InjectMocks
    private RecommendationRequestListener recommendationRequestListener;
    private UserDto userDto;
    private EventRecommendationRequestDto event;
    private final String message = "You got a new recommendation request";

    @BeforeEach
    public void init() {
        List<NotificationService> notificationServices = new ArrayList<>(List.of(telegramService));
        List<MessageBuilder<EventRecommendationRequestDto>> messageBuilders = new ArrayList<>(List.of(messageBuilder));
        recommendationRequestListener = new RecommendationRequestListener(objectMapper, userServiceClient, notificationServices, messageBuilders);

        event = EventRecommendationRequestDto.builder()
                .requesterId(1L)
                .receiverId(2L)
                .recommendationId(1L)
                .build();

        userDto = UserDto.builder()
                .id(2L)
                .preferredContact(PreferredContact.TELEGRAM)
                .build();
    }

    @Test
    public void sendNotificationTest() {
        Mockito.when(userServiceClient.getUser(event.getReceiverId())).thenReturn(userDto);
        Mockito.when(telegramService.getPreferredContact()).thenReturn(PreferredContact.TELEGRAM);
        recommendationRequestListener.sendNotification(event.getReceiverId(), message);

        Mockito.verify(telegramService, Mockito.times(1)).sendNotification(message);
    }

    @Test
    public void getMessageTest() {
        Mockito.when(messageBuilder.buildMessage(event, Locale.ENGLISH)).thenReturn(message);
        Mockito.when(messageBuilder.supports(RecommendationRequestMessageBuilder.class)).thenReturn(true);
        assertEquals(message, recommendationRequestListener.getMessage(RecommendationRequestMessageBuilder.class, event));
    }

    @Test
    public void getMessageThrowsExceptionTest() {
        Mockito.when(messageBuilder.supports(RecommendationRequestMessageBuilder.class)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> recommendationRequestListener.getMessage(RecommendationRequestMessageBuilder.class, null));
    }

}