package faang.school.notificationservice.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.events.CommentEvent;
import faang.school.notificationservice.messaging.CommentEventMessageBuilder;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CommentEventListenerTest {

    private final ObjectMapper objectMapper = spy(new ObjectMapper());
    private final UserServiceClient userServiceClient = mock(UserServiceClient.class);
    private final NotificationService notificationService = mock(NotificationService.class);
    private final List<NotificationService> notificationServices = List.of(notificationService);
    private final CommentEventMessageBuilder commentEventMessageBuilder = mock(CommentEventMessageBuilder.class);
    private final List<MessageBuilder<CommentEvent>> messageBuilders = List.of(commentEventMessageBuilder);
    private final CommentEventListener commentEventListener
            = new CommentEventListener(objectMapper, userServiceClient, notificationServices, messageBuilders);

    @Test
    void testOnMessage() throws IOException {
        ObjectMapper objectMapper1 = new ObjectMapper();
        CommentEvent commentEvent = CommentEvent.builder()
                .postId(1L)
                .postAuthorId(11L)
                .authorId(21L)
                .commentId(31L)
                .commentContent("Content")
                .build();
        Message message = new DefaultMessage(new byte[0], objectMapper1.writeValueAsBytes(commentEvent));

        doReturn(CommentEvent.class).when(commentEventMessageBuilder).supportsEvent();
        when(commentEventMessageBuilder.buildMessage(any(),any())).thenReturn("");
        when(userServiceClient.getUser(anyLong())).thenReturn(
                UserDto.builder()
                        .preference(UserDto.PreferredContact.EMAIL)
                        .build());
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        commentEventListener.onMessage(message, new byte[0]);

        verify(objectMapper, times(1)).readValue(message.getBody(), CommentEvent.class);
        verify(commentEventMessageBuilder, times(1)).buildMessage(any(), any());
        verify(userServiceClient, times(1)).getUser(11L);
        verify(notificationService, times(1)).getPreferredContact();
        verify(notificationService, times(1)).send(any(), any());
    }

}