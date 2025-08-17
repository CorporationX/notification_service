package faang.school.notificationservice.service;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.RecommendationRequestEvent;
import faang.school.notificationservice.messaging.RecommendationRequestMessageBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationConsumerTest {

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private TelegramService telegramService;

    @Mock
    private RecommendationRequestMessageBuilder recommendationRequestMessageBuilder;

    @InjectMocks
    private NotificationConsumer notificationConsumer;

    private RecommendationRequestEvent event;
    private UserDto user;

    @BeforeEach
    void setUp() {
        event = new RecommendationRequestEvent();
        event.setRequesterId(1L);
        event.setReceiverId(2L);
        event.setRecommendationRequestId(3L);

        user = new UserDto();
        user.setId(2L);
        user.setUsername("testuser");
        user.setLocale(Locale.ENGLISH);
    }

    @Test
    void consumeShouldProcessEventAndSendTelegramNotification() {
        String expectedMessage = "Test notification message";
        
        when(userServiceClient.getUser(2L)).thenReturn(user);
        when(recommendationRequestMessageBuilder.buildMessage(eq(event), any(Locale.class)))
                .thenReturn(expectedMessage);

        notificationConsumer.consume(event);

        verify(userServiceClient).getUser(2L);
        verify(recommendationRequestMessageBuilder).buildMessage(eq(event), any(Locale.class));
        verify(telegramService).send(any(UserDto.class), eq(expectedMessage));
    }
}