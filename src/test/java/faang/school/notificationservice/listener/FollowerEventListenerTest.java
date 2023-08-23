package faang.school.notificationservice.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.builder.FollowerMessageBuilder;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.model.EventType;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FollowerEventListenerTest {
    @InjectMocks
    FollowerEventListener followerEventListener;
    @Mock
    ObjectMapper objectMapper;
    @Mock
    FollowerMessageBuilder messageBuilder;
    @Mock
    UserServiceClient userServiceClient;
    @Mock
    Message message;
    @Mock
    NotificationService notificationService;
    @Mock
    List<NotificationService> notificationServices;

    @Test
    public void testOnMessage() throws IOException {
        FollowerEvent event = new FollowerEvent(EventType.FOLLOWER, new Date(), 1L, 2L);
        String text = "test";
        UserDto user = UserDto.builder().id(1L).build();

        when(message.getBody()).thenReturn(new byte[0]);
        when(objectMapper.readValue(message.getBody(), FollowerEvent.class)).thenReturn(event);
        when(messageBuilder.getText(event)).thenReturn(text);
        when(userServiceClient.getUser(any(Long.class))).thenReturn(user);
        when(notificationServices.stream()).thenReturn(Stream.of(notificationService));

        followerEventListener.onMessage(message, new byte[0]);

        verify(notificationService, Mockito.times(1)).send(Mockito.eq(user), Mockito.eq(text));
    }
}
