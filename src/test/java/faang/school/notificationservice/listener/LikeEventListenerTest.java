package faang.school.notificationservice.listener;

import faang.school.notificationservice.dto.like.LikeEvent;
import faang.school.notificationservice.service.EmailService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LikeEventListenerTest {
    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private EmailService emailService;

    @Mock
    private MessageBuilder<LikeEvent> messageBuilder;

    @Mock
    private Message message;

    @BeforeEach
    void setUp() {
        likePostEventListener = new LikeEventListener(
                objectMapper, userServiceClient, List.of(emailService), List.of(messageBuilder));
    }

    private LikeEventListener likePostEventListener;

    @Test
    void testOnMessage() throws IOException {
        LikeEvent event = new LikeEvent(1L, 2L, 3L);
        UserDto userReceiver = UserDto.builder().id(1L).build();
        userReceiver.setPreference(UserDto.PreferredContact.EMAIL);

        when(objectMapper.readValue(any(byte[].class), eq(LikeEvent.class))).thenReturn(event);

        when(userServiceClient.getUser(event.likeUserId()))
                .thenReturn(UserDto.builder().username("Alex").build());

        when(messageBuilder.buildMessage(event, Locale.getDefault()))
                .thenReturn("You've got a new like from user: ");

        when(messageBuilder.getInstance()).thenReturn(LikeEvent.class);

        when(userServiceClient.getUser(1L)).thenReturn(userReceiver);

        when(emailService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        when(message.getBody()).thenReturn(new byte[0]);

        likePostEventListener.onMessage(message, new byte[0]);

        verify(emailService, times(1)).send(any(), eq("You've got a new like from user: Alex"));
    }

}
