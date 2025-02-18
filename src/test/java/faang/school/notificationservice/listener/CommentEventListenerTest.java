package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.CommentEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.Acknowledgment;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentEventListenerTest {
    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private MessageBuilder<CommentEvent> messageBuilder;

    @Mock
    private NotificationService notificationService;

    @Mock
    private Acknowledgment acknowledgment;

    @Mock
    private ConsumerRecord<String, String> consumerRecord;

    private CommentEventListener commentEventListener;

    @BeforeEach
    void setUp() {
        List<MessageBuilder<CommentEvent>> messageBuilders = List.of(messageBuilder);
        List<NotificationService> notificationServices = List.of(notificationService);
        commentEventListener = new CommentEventListener(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Test
    void testOnMessage() {
        CommentEvent commentEvent = CommentEvent.builder()
                .content("Test comment")
                .postId(1L)
                .authorId(100L)
                .build();
        String jsonValue = "{\"content\":\"Test comment\",\"postId\":1,\"authorId\":100}";

        when(consumerRecord.value()).thenReturn(jsonValue);
        when(objectMapper.convertValue(jsonValue, CommentEvent.class)).thenReturn(commentEvent);
        when(messageBuilder.getInstance()).thenReturn((Class) CommentEvent.class);
        when(messageBuilder.buildMessage(commentEvent, Locale.UK)).thenReturn("Built message");

        // Настраиваем получение пользователя
        UserDto userDto = UserDto.builder()
                .id(100L)
                .username("user")
                .email("user@example.com")
                .phone("123456")
                .preference(UserDto.PreferredContact.EMAIL)
                .build();
        when(userServiceClient.getUser(100L)).thenReturn(userDto);
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        commentEventListener.onMessage(consumerRecord, acknowledgment);

        verify(objectMapper).convertValue(jsonValue, CommentEvent.class);
        verify(messageBuilder).getInstance();
        verify(messageBuilder).buildMessage(commentEvent, Locale.UK);
        verify(userServiceClient).getUser(100L);
        verify(notificationService).getPreferredContact();
        verify(notificationService).send(userDto, "Built message");
        verify(acknowledgment).acknowledge();
    }
}