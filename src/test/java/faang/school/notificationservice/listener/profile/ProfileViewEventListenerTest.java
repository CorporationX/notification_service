package faang.school.notificationservice.listener.profile;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.profile.ProfileViewEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProfileViewEventListenerTest {

    @InjectMocks
    ProfileViewEventListener profileViewEventListener;

    @Mock
    ObjectMapper objectMapper;
    @Mock
    UserServiceClient userServiceClient;
    List<NotificationService> notificationServices;
    List<MessageBuilder<ProfileViewEvent>> messageBuilders;
    @Mock
    NotificationService notificationServicePositive;
    @Mock
    NotificationService notificationServiceNegative;
    @Mock
    Message message;

    @BeforeEach
    public void setUp() {
        MessageBuilder messageBuilder = mock(MessageBuilder.class);
        when(messageBuilder.getInstance()).thenReturn(ProfileViewEvent.class);
        notificationServices = List.of(notificationServicePositive, notificationServiceNegative);
        messageBuilders = List.of(messageBuilder);
        profileViewEventListener = new ProfileViewEventListener(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Test
    void testOnMessage() throws IOException {
        String testMessage = "testMessage";
        long viewingUserId = 1L;
        ProfileViewEvent event = ProfileViewEvent.builder().viewingId(viewingUserId).viewerId(2L).build();
        byte[] body = "test".getBytes();
        when(message.getBody()).thenReturn(body);
        when(objectMapper.readValue(body, ProfileViewEvent.class)).thenReturn(event);
        when(messageBuilders.get(0).buildMessage(event, Locale.UK)).thenReturn(testMessage);
        UserDto user = UserDto.builder().id(viewingUserId).preference(UserDto.PreferredContact.TELEGRAM).build();
        when(userServiceClient.getUser(viewingUserId)).thenReturn(user);
        when(notificationServicePositive.getPreferredContact()).thenReturn(UserDto.PreferredContact.TELEGRAM);
        profileViewEventListener.onMessage(message, null);
        verify(notificationServicePositive, times(1)).send(user, testMessage);
        verify(notificationServiceNegative, never()).send(user, testMessage);

    }

    @Test
    void testNoMessageBuilderFound() throws IOException {
        long viewingUserId = 1L;
        ProfileViewEvent event = ProfileViewEvent.builder().viewingId(viewingUserId).viewerId(2L).build();
        byte[] body = "test".getBytes();
        when(message.getBody()).thenReturn(body);
        when(objectMapper.readValue(body, ProfileViewEvent.class)).thenReturn(event);
        messageBuilders = new ArrayList<>();
        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> profileViewEventListener.onMessage(message, null));
        assertEquals("No message builder found for event: " + event.getClass().getName(), exception.getMessage());
    }

    @Test
    void testNoNotificationServiceFound() throws IOException {
        String testMessage = "testMessage";
        long viewingUserId = 1L;
        ProfileViewEvent event = ProfileViewEvent.builder().viewingId(viewingUserId).viewerId(2L).build();
        byte[] body = "test".getBytes();
        when(message.getBody()).thenReturn(body);
        when(objectMapper.readValue(body, ProfileViewEvent.class)).thenReturn(event);
        when(messageBuilders.get(0).buildMessage(event, Locale.UK)).thenReturn(testMessage);
        UserDto user = UserDto.builder().id(viewingUserId).preference(UserDto.PreferredContact.TELEGRAM).build();
        when(userServiceClient.getUser(viewingUserId)).thenReturn(user);

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> profileViewEventListener.onMessage(message, null));
        assertEquals("No notification service found for the user preferred communication method : "
                + user.getPreference(), exception.getMessage());
    }

}