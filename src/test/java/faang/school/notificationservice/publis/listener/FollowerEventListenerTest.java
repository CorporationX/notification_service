package faang.school.notificationservice.publis.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.follower.FollowerEventDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.publis.listener.follower.FollowerEventListener;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.test_data.follower.TestDataFollowerEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FollowerEventListenerTest {
    @Mock
    private Message message;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private MessageBuilder<FollowerEventDto> messageBuilder;
    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private FollowerEventListener followerEventListener;

    private UserDto followee;
    private FollowerEventDto followerEventDto;

    @BeforeEach
    void setUp() {
        followerEventListener = new FollowerEventListener(
                objectMapper,
                userServiceClient,
                Collections.singletonList(messageBuilder),
                Collections.singletonList(notificationService)
        );

        TestDataFollowerEvent testDataFollowerEvent = new TestDataFollowerEvent();

        followee = testDataFollowerEvent.getFollowee();
        followerEventDto = testDataFollowerEvent.getFollowerEventDto();
    }

    @Test
    void testOnMessage_Success() throws JsonProcessingException {
        String messageBody = followerEventDto.toString();
        byte[] pattern = {};
        String notificationContent = "Test follower notification";

        when(message.getBody()).thenReturn(messageBody.getBytes());
        when(objectMapper.readValue(messageBody, FollowerEventDto.class)).thenReturn(followerEventDto);
        when(messageBuilder.getInstance()).thenReturn((Class) FollowerEventDto.class);
        when(messageBuilder.buildMessage(followerEventDto, Locale.getDefault())).thenReturn(notificationContent);
        when(userServiceClient.getUser(followerEventDto.getFolloweeId())).thenReturn(followee);
        when(notificationService.getPreferredContact()).thenReturn(followee.getPreference());

        followerEventListener.onMessage(message, pattern);

        verify(objectMapper, atLeastOnce()).readValue(messageBody, FollowerEventDto.class);
        verify(notificationService, atLeastOnce()).send(followee, notificationContent);
    }

    @Test
    void testWhenNoMessageBuilderFound() {
        FollowerEventDto followerEventDto = new FollowerEventDto().builder().followerId(1L).followeeId(2L).build();
        List<MessageBuilder<FollowerEventDto>> emptyMessageBuilders = Collections.emptyList();

        followerEventListener = new FollowerEventListener(
                objectMapper,
                userServiceClient,
                emptyMessageBuilders,
                List.of(notificationService)
        );

        assertThrows(IllegalArgumentException.class, () ->
                followerEventListener.getMessage(followerEventDto, Locale.getDefault())
        );
    }
}
