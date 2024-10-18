package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.feign.UserServiceClient;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.model.enums.PreferredContact;
import faang.school.notificationservice.model.event.CommentEvent;
import faang.school.notificationservice.service.MessageBuilder;
import faang.school.notificationservice.service.impl.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private EmailService emailService;

    @Mock
    private MessageBuilder<CommentEvent> messageBuilder;

    @Mock
    private Message message;

    @BeforeEach
    void setUp() {
        commentEventListener = new CommentEventListener(
                objectMapper, List.of(emailService), userServiceClient, List.of(messageBuilder));
    }

    private CommentEventListener commentEventListener;

    @Test
    void testOnMessage() throws IOException {
        CommentEvent commentEvent = new CommentEvent(1L, 2L, 3L, "someText", 4L);
        UserDto userDto = new UserDto();
        userDto.setPreference(PreferredContact.EMAIL);

        when(objectMapper.readValue(any(byte[].class), eq(CommentEvent.class))).thenReturn(commentEvent);
        when(messageBuilder.buildMessage(commentEvent, Locale.ENGLISH)).thenReturn("Post was commented!");
        when(messageBuilder.getSupportedClass()).thenReturn(CommentEvent.class);
        when(userServiceClient.getUser(anyLong())).thenReturn(userDto);
        when(emailService.getPreferredContact()).thenReturn(PreferredContact.EMAIL);
        when(message.getBody()).thenReturn(new byte[0]);

        commentEventListener.onMessage(message, new byte[0]);

        verify(emailService, times(1)).send(any(), eq("Post was commented!"));
    }
}