package faang.school.notificationservice.listener.like;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.dto.publishable.LikeEvent;
import faang.school.notificationservice.listener.like.LikeEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;


@ExtendWith(MockitoExtension.class)
public class LikeEventListenerTest {
    @Mock
    private NotificationService notificationService;
    @Mock
    private ObjectMapper mapper;
    @Mock
    private MessageBuilder<LikeEvent> messageBuilder;
    @Mock
    private UserServiceClient userServiceClient;
    private Class<LikeEvent> eventType = LikeEvent.class;
    private LikeEventListener eventListener;
    private long actorId = 1L;
    private long receiverId = 2L;
    private String actorName = "Gena";
    private String receiverName = "Viet";
    private UserDto actor = UserDto.builder().build();
    private UserDto receiver = UserDto.builder().build();
    private LikeEvent event = LikeEvent.builder()
            .actorId(actorId)
            .receiverId(receiverId)
            .build();
    @BeforeEach
    public void setUp() {
        eventListener = new LikeEventListener(
                List.of(notificationService),
                mapper,
                messageBuilder,
                userServiceClient
        );

        actor.setUsername(actorName);
        receiver.setUsername(receiverName);
        lenient().when(userServiceClient.getUser(actorId)).thenReturn(actor);
        lenient().when(userServiceClient.getUser(receiverId)).thenReturn(receiver);
    }

    @Test
    public void testGetNotifiedUsers() {
        List<UserDto> result = eventListener.getNotifiedUsers(event);

        assertEquals(List.of(receiver), result);
    }

    @Test
    public void testGetArgs() {
        Object[] result = eventListener.getArgs(event);

        assertEquals(receiverName, result[0]);
        assertEquals(actorName, result[1]);
        assertEquals(2, result.length);
    }
}
