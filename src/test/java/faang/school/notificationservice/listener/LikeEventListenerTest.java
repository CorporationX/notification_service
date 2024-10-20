package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.event.LikeEvent;
import faang.school.notificationservice.messaging.LikeMessageBuilder;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
class LikeEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private LikeMessageBuilder messageBuilder;

    @Mock
    private Message message;

    @Mock
    private NotificationService telegramService;


    @InjectMocks
    private LikeEventListener eventListener;

    private UserDto userDto;
    private Locale locale;
    private LikeEvent likeEvent;
    private List<NotificationService> notificationServiceList;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .id(1L)
                .preference(UserDto.PreferredContact.TELEGRAM)
                .build();

        locale = Locale.getDefault();

        likeEvent = LikeEvent.builder()
                .postAuthorId(1L)
                .likeAuthorId(1L)
                .postId(1L)
                .build();

        notificationServiceList = List.of(telegramService);
        eventListener = new LikeEventListener(objectMapper, userServiceClient,
                messageBuilder, notificationServiceList);
    }

    @Test
    void testOnMessageOk() throws IOException {
        //given
        String event = "postAuthorId : 1, likeAuthorId : 1, postId : 1";
        Mockito.when(message.getBody()).thenReturn(event.getBytes());
        Mockito.when(objectMapper.readValue(Mockito.any(byte[].class), Mockito.eq(LikeEvent.class)))
                .thenReturn(likeEvent);
        Mockito.when(messageBuilder.buildMessage(likeEvent, locale))
                .thenReturn("hello");
        Mockito.when(userServiceClient.getUser(Mockito.anyLong()))
                .thenReturn(userDto);
        Mockito.when(telegramService.getPreferredContact()).thenReturn(UserDto.PreferredContact.TELEGRAM);

        //when
        eventListener.onMessage(message, new byte[0]);

        //then
        Mockito.verify(message, Mockito.times(1)).getBody();
        Mockito.verify(objectMapper, Mockito.times(1))
                .readValue(Mockito.any(byte[].class), Mockito.eq(LikeEvent.class));
        Mockito.verify(messageBuilder, Mockito.times(1))
                .buildMessage(likeEvent, locale);
        Mockito.verify(userServiceClient, Mockito.times(1)).getUser(Mockito.anyLong());
    }

    @Test
    void testNoPassingNotificationTypes() throws IOException {
        String event = "postAuthorId : 1, likeAuthorId : 1, postId : 1";
        Mockito.when(message.getBody()).thenReturn(event.getBytes());
        Mockito.when(objectMapper.readValue(Mockito.any(byte[].class), Mockito.eq(LikeEvent.class)))
                .thenReturn(likeEvent);
        Mockito.when(messageBuilder.buildMessage(likeEvent, locale))
                .thenReturn("hello");
        Mockito.when(userServiceClient.getUser(Mockito.anyLong()))
                .thenReturn(userDto);
        Mockito.when(telegramService.getPreferredContact()).thenReturn(UserDto.PreferredContact.SMS);

        assertThrows(IllegalArgumentException.class, () -> eventListener.onMessage(message, new byte[0]));
    }
}