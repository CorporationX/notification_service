package faang.school.notificationservice.listener.like;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.like.LikeEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Locale;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LikeListenerTest {

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private MessageBuilder<LikeEvent> messageBuilder;
    @Mock
    private NotificationService notificationService;
    @Mock
    private Message redisMessage;
    @InjectMocks
    private LikeListener likeListener;
    private LikeEvent likeEvent;
    private String jsonString = "{\"authorId\":1L,\"receivedId\":2L,\"likeId\":3L}";


    @BeforeEach
    void setUp() {
        likeListener = new LikeListener(
                objectMapper,
                userServiceClient,
                Collections.singletonList(messageBuilder),
                Collections.singletonList(notificationService)
        );
        likeEvent = LikeEvent.builder()
                .authorId(1L)
                .receivedId(2L)
                .likeId(3L)
                .build();
    }

    @Test
    void testOnMessage() throws Exception {
        when(redisMessage.getBody()).thenReturn(jsonString.getBytes());
        when(objectMapper.readValue(Mockito.any(byte[].class), Mockito.eq(LikeEvent.class))).thenReturn(likeEvent);

        String notificationContent = "Like test";
        when(messageBuilder.getInstance()).thenReturn(LikeEvent.class);
        when(messageBuilder.buildMessage(likeEvent, Locale.UK)).thenReturn(notificationContent);

        UserDto userDto = new UserDto();
        userDto.setPreference(UserDto.PreferredContact.EMAIL);
        when(userServiceClient.getUser(2L)).thenReturn(userDto);
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        likeListener.onMessage(redisMessage, null);

        verify(redisMessage, times(1)).getBody();
        verify(objectMapper, times(1))
                .readValue(Mockito.any(byte[].class), Mockito.eq(LikeEvent.class));
        verify(messageBuilder, times(1)).getInstance();
        verify(messageBuilder, times(1)).buildMessage(likeEvent, Locale.UK);
    }
}