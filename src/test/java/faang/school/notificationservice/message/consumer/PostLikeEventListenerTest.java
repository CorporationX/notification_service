package faang.school.notificationservice.message.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.builder.message.MessageBuilder;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.user.Language;
import faang.school.notificationservice.dto.user.PreferredContact;
import faang.school.notificationservice.dto.user.UserForNotificationDto;
import faang.school.notificationservice.message.event.PostLikeEvent;
import faang.school.notificationservice.service.notification.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostLikeEventListenerTest {

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

    private PostLikeEventListener listener;

    @BeforeEach
    void setUp() {
        listener = new PostLikeEventListener(
                mapper,
                userServiceClient,
                List.of(notificationService),
                messageBuilder
        );
    }

    @Test
    void onMessage_ProcessesMessageCorrectly() throws IOException {
        Long receiverId = 1L;
        Long authorId = 2L;
        PostLikeEvent event = new PostLikeEvent(receiverId, authorId, "Joe", 1L, LocalDateTime.now());
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
        when(userServiceClient.getUserForNotificationById(event.getReceiverId())).thenReturn(receiver);
        when(messageBuilder.build(event, receiver.getLocaleFromLanguage())).thenReturn(message);
        when(notificationService.getPreferredContact()).thenReturn(receiver.preference());

        listener.onMessage(redisMessage, new byte[0]);

        verify(notificationService).send(receiver, message);
    }
}
