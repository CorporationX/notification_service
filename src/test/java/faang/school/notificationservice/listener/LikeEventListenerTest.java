package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.LikeEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.LikeMessageBuilder;
import faang.school.notificationservice.service.EmailService;
import faang.school.notificationservice.listener.LikeEventListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikeEventListenerTest {
    @Mock
    private EmailService emailService;

    @Mock
    private LikeMessageBuilder likeMessageBuilder;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private Message message;

    @InjectMocks
    private LikeEventListener likeEventListener;

    @Test
    void onMessageShouldProcessMessageAndSendEmail() throws Exception {
        String json = "{\"postAuthorId\":1,\"likeAuthorId\":2,\"postId\":44}";
        LikeEvent likeEvent = LikeEvent.builder()
                .postAuthorId(1L)
                .likeAuthorId(2L)
                .postId(44L)
                .build();

        UserDto user = new UserDto();
        user.setId(1L);
        user.setPreference(UserDto.PreferredContact.EMAIL);

        String expectedMessage = "User with id: 2 liked your post with id: 44";

        when(message.getBody()).thenReturn(json.getBytes(StandardCharsets.UTF_8));
        when(objectMapper.readValue(eq(json), eq(LikeEvent.class))).thenReturn(likeEvent);
        when(userServiceClient.getUser(1L)).thenReturn(user);
        when(likeMessageBuilder.buildMessage(eq(likeEvent), eq(Locale.US))).thenReturn(expectedMessage);

        likeEventListener.onMessage(message, null);

        ArgumentCaptor<UserDto> userCaptor = ArgumentCaptor.forClass(UserDto.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        verify(emailService).send(userCaptor.capture(), messageCaptor.capture());

        assertEquals(user, userCaptor.getValue());
        assertEquals(expectedMessage, messageCaptor.getValue());
    }
}