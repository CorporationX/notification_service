package faang.school.notificationservice.messaging.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.messaging.listener.FollowerEventListener;
import faang.school.notificationservice.messaging.message_builder.FollowerMessageBuilder;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.model.EventType;
import faang.school.notificationservice.service.email.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FollowerEventListenerTest {
    @InjectMocks
    FollowerEventListener followerEventListener;
    @Mock
    FollowerMessageBuilder followerMessageBuilder;
    @Mock
    UserServiceClient userServiceClient;
    @Mock
    MessageSource messageSource;
    @Mock
    Message message;
    @Mock
    ObjectMapper objectMapper;
    @Mock
    EmailService emailService;

    @Test
    public void testOnMessage() throws IOException {
        FollowerEvent event = FollowerEvent.builder()
                .eventType(EventType.FOLLOWER)
                .followerId(1L)
                .followeeId(2L)
                .build();
        followerMessageBuilder = new FollowerMessageBuilder(userServiceClient, messageSource);
        Locale locale = Locale.getDefault();
        String notificationText = "test";
        UserDto user = UserDto.builder()
                .preference(UserDto.PreferredContact.EMAIL)
                .build();
        followerEventListener = new FollowerEventListener(objectMapper, userServiceClient, List.of(followerMessageBuilder), List.of(emailService));

        when(objectMapper.readValue(message.getBody(), FollowerEvent.class)).thenReturn(event);
        when(userServiceClient.getUser(event.getFollowerId())).thenReturn(user);
        when(followerMessageBuilder.buildMessage(event, locale)).thenReturn(notificationText);
        when(userServiceClient.getUser(event.getFolloweeId())).thenReturn(user);
        when(emailService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        followerEventListener.onMessage(message, new byte[0]);

        verify(emailService, Mockito.times(1)).send(user, notificationText);
    }
}
