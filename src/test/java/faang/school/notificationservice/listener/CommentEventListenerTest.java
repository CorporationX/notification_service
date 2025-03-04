package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.CommentEvent;
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
class CommentEventListenerTest {

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private MessageBuilder<CommentEvent> messageBuilder;

    @Mock
    private NotificationService notificationService;

    private CommentEventListener commentEventListener;

    @BeforeEach
    void setUp() {
        List<MessageBuilder<CommentEvent>> messageBuilders = List.of(messageBuilder);
        List<NotificationService> notificationServices = List.of(notificationService);
        commentEventListener = new CommentEventListener(messageBuilders, notificationServices, userServiceClient);
    }

    @Test
    void testOnMessage() {
        long userId = 100;
        CommentEvent commentEvent = CommentEvent.builder()
                .content("Test comment")
                .postId(1L)
                .authorId(userId)
                .build();

        when(messageBuilder.getInstance()).thenReturn((Class) CommentEvent.class);
        when(messageBuilder.buildMessage(commentEvent, Locale.UK)).thenReturn("Built message");

        UserDto userDto = UserDto.builder()
                .id(userId)
                .username("user")
                .email("user@example.com")
                .phone("123456")
                .preference(UserDto.PreferredContact.EMAIL)
                .build();
        when(userServiceClient.getUser(userId)).thenReturn(userDto);
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

       commentEventListener.onMessage(commentEvent);

        verify(messageBuilder).getInstance();
        verify(messageBuilder).buildMessage(commentEvent, Locale.UK);
        verify(userServiceClient).getUser(userId);
        verify(notificationService).getPreferredContact();
        verify(notificationService).send(userDto, "Built message");
    }
}