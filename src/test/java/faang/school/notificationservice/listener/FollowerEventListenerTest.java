package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.FollowerEvent;
import faang.school.notificationservice.messaging.FollowerMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FollowerEventListenerTest {
    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private FollowerMessageBuilder followerMessageBuilder;

    @Mock
    private Message message;

    @Mock
    private List<NotificationService> notificationServices;

    @InjectMocks
    private FollowerEventListener followerEventListener;

    @Test
    public void testOnMessage() throws Exception {
        //Arrange
        FollowerEvent followerEvent = FollowerEvent.builder()
                .followerId(1L)
                .subscriberId(2L)
                .build();

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("testUser");

        String messageBody = "{\"subscriberId\":1,\"followerId\":2}";
        String messageText = "New follower!";

        when(objectMapper.readValue(messageBody, FollowerEvent.class)).thenReturn(followerEvent);
        when(userServiceClient.getUser(followerEvent.getSubscriberId())).thenReturn(userDto);
        when(followerMessageBuilder.buildMessage(eq(followerEvent), any(Locale.class))).thenReturn(messageText);
        when(message.getBody()).thenReturn(messageBody.getBytes());

        //act
        followerEventListener.onMessage(message, null);

        //Assert
        verify(objectMapper).readValue(messageBody, FollowerEvent.class);
        verify(userServiceClient).getUser(followerEvent.getSubscriberId());
        verify(followerMessageBuilder).buildMessage(followerEvent, Locale.ENGLISH);
        verify(notificationServices).forEach(any());
    }

    @Test
    public void testOnMessageThrowsIOException() throws Exception {
        //Arrange
        String messageBody = "{\"subscriberId\":1,\"followerId\":2}";
        when(objectMapper.readValue(messageBody, FollowerEvent.class)).thenThrow(new RuntimeException());
        when(message.getBody()).thenReturn(messageBody.getBytes());

        //Act & Assert
        assertThrows(RuntimeException.class, () -> followerEventListener.onMessage(message, null));
    }
}
