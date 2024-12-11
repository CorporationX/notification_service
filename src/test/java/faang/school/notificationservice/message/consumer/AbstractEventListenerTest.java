package faang.school.notificationservice.message.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.builder.message.MessageBuilder;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.user.Language;
import faang.school.notificationservice.dto.user.PreferredContact;
import faang.school.notificationservice.dto.user.UserForNotificationDto;
import faang.school.notificationservice.exceptions.PreferredContactNotExistException;
import faang.school.notificationservice.message.event.PostLikeEvent;
import faang.school.notificationservice.service.notification.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.data.redis.connection.Message;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class AbstractEventListenerTest {

    @Mock
    private ObjectMapper mapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private NotificationService notificationService;

    @Mock
    private MessageBuilder<PostLikeEvent> messageBuilder;

    @Mock
    private Message redisMessage;

    private List<NotificationService> notificationServices;
    private PostLikeEventListener eventListener;

    private Long receiverId;

    @BeforeEach
    void setUp() {
        receiverId = 1L;
        notificationServices = List.of(notificationService);
        eventListener = new PostLikeEventListener(mapper, userServiceClient, notificationServices, messageBuilder);
    }

    @Test
    void testHandleEvent_SuccessfullyProcessesMessage() throws IOException {
        PostLikeEvent event = new PostLikeEvent();
        event.setReceiverId(receiverId);
        UserForNotificationDto receiver = UserForNotificationDto.builder()
                .id(receiverId)
                .username("ya_kokin")
                .email("kolyasik@gmail.com")
                .phone("+1234567890")
                .language(Language.EN)
                .preference(PreferredContact.PHONE)
                .build();
        String message = "Test message";

        when(redisMessage.getBody()).thenReturn("messageBody".getBytes());
        when(mapper.readValue(any(byte[].class), eq(PostLikeEvent.class))).thenReturn(event);
        when(userServiceClient.getUserForNotificationById(1L)).thenReturn(receiver);
        when(messageBuilder.build(event, receiver.getLocaleFromLanguage())).thenReturn(message);
        when(notificationService.getPreferredContact()).thenReturn(receiver.preference());

        eventListener.handleEvent(redisMessage, PostLikeEvent.class, PostLikeEvent::getReceiverId);

        verify(notificationService).send(receiver, message);
    }

    @Test
    void testHandleEvent_WhenMapperThrowsException_LogsError() throws IOException {
        when(redisMessage.getBody()).thenReturn("messageBody".getBytes());
        when(mapper.readValue(any(byte[].class), eq(PostLikeEvent.class)))
                .thenThrow(new IOException("Test exception"));

        try {
            eventListener.handleEvent(redisMessage, PostLikeEvent.class, PostLikeEvent::getReceiverId);
        } catch (Exception e) {
            verify(notificationService, never()).send(any(), any());
        }
    }

    @Test
    void testHandleEvent_WhenMapperThrowsException_ThrowsException() throws IOException {
        when(redisMessage.getBody()).thenReturn("messageBody".getBytes());
        when(mapper.readValue(any(byte[].class), eq(PostLikeEvent.class)))
                .thenThrow(new IOException("Test exception"));

        assertThrows(RuntimeException.class,
                () -> eventListener.handleEvent(redisMessage, PostLikeEvent.class, PostLikeEvent::getReceiverId));
    }

    @Test
    void testSendNotification_WhenNoMatchingService_ThrowsException() {
        UserForNotificationDto receiver = UserForNotificationDto.builder()
                .id(receiverId)
                .username("ya_kokin")
                .email("kolyasik@gmail.com")
                .phone("+1234567890")
                .language(Language.EN)
                .preference(PreferredContact.PHONE)
                .build();
        when(notificationService.getPreferredContact()).thenReturn(PreferredContact.EMAIL);

        assertThrows(PreferredContactNotExistException.class,
                () -> eventListener.sendNotification(receiver, "message"));
    }

    @Test
    void testSendNotification_WithMatchingService_SendsNotification() {
        UserForNotificationDto receiver = UserForNotificationDto.builder()
                .id(receiverId)
                .username("ya_kokin")
                .email("kolyasik@gmail.com")
                .phone("+1234567890")
                .language(Language.EN)
                .preference(PreferredContact.PHONE)
                .build();
        String message = "Test message";
        when(notificationService.getPreferredContact()).thenReturn(receiver.preference());

        eventListener.sendNotification(receiver, message);

        verify(notificationService).send(receiver, message);
    }
}
