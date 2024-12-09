package faang.school.notificationservice.message.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.builder.message.MessageBuilder;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.Language;
import faang.school.notificationservice.dto.PreferredContact;
import faang.school.notificationservice.dto.UserForNotificationDto;
import faang.school.notificationservice.exceptions.PreferredContactNotExistException;
import faang.school.notificationservice.message.event.RecommendationReceivedEvent;
import faang.school.notificationservice.service.notification.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecommendationReceivedEventListenerTest {

    private RecommendationReceivedEventListener recommendationReceivedEventListener;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private MessageBuilder<RecommendationReceivedEvent> messageBuilder;

    @Mock
    private Message message;

    @Mock
    private JsonNode jsonNode;

    private NotificationService notificationService;

    private RecommendationReceivedEvent event;
    private UserForNotificationDto user;
    private String notificationMessage;

    @BeforeEach
    public void setUp() throws IOException {
        notificationService = Mockito.mock(NotificationService.class);
        List<NotificationService> notificationServices = List.of(notificationService);

        recommendationReceivedEventListener = new RecommendationReceivedEventListener(
                objectMapper,
                userServiceClient,
                notificationServices,
                messageBuilder
        );

        event = RecommendationReceivedEvent.builder()
                .receiverId(1L)
                .recommenderUserId(2L)
                .recommendationId(3L)
                .build();
        user = UserForNotificationDto
                .builder()
                .id(1L)
                .email("someemail@gmail.com")
                .preference(PreferredContact.EMAIL)
                .locale(Language.EN)
                .build();
        notificationMessage = "Congrats! Somebody recommended you!";

        when(objectMapper.readTree(message.getBody()))
                .thenReturn(jsonNode);
        when(objectMapper.convertValue(jsonNode, RecommendationReceivedEvent.class))
                .thenReturn(event);
        when(userServiceClient.getUserByIdForNotification(1L))
                .thenReturn(user);
        when(messageBuilder.build(event, user.getLocaleFromLanguage()))
                .thenReturn(notificationMessage);
    }

    @Test
    public void testOnMessage() throws IOException {
        // arrange
        when(notificationService.getPreferredContact())
                .thenReturn(PreferredContact.EMAIL);

        // act
        recommendationReceivedEventListener.onMessage(message, new byte[]{});

        // assert
        verify(notificationService).send(user, notificationMessage);
    }

    @Test
    public void testOnMessageThrowsPreferredContactNotFoundException() throws IOException {
        // act and assert
        assertThrows(PreferredContactNotExistException.class,
                () -> recommendationReceivedEventListener.onMessage(message, new byte[]{}));
    }
}
