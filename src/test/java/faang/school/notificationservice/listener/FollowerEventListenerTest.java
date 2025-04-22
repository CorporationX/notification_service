package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FollowerEventListenerTest {

    @Mock
    ObjectMapper objectMapper;
    @Mock
    UserServiceClient userServiceClient;
    @Mock
    MessageBuilder<FollowerEvent> builder;
    @Mock
    NotificationService notifier;
    @Mock
    Message message;

    private FollowerEventListener listener;
    private FollowerEvent event;
    private UserDto follower;
    private UserDto followee;

    @BeforeEach
    void setUp() throws Exception {
        event = FollowerEvent.builder()
                .followerId(10L)
                .followeeId(2L)
                .timestamp(LocalDateTime.now())
                .build();

        when(objectMapper.readValue(any(byte[].class), eq(FollowerEvent.class)))
                .thenReturn(event);

        follower = UserDto.builder()
                .id(10L)
                .username("User1")
                .email("user1@example.com")
                .phone("+123456789")
                .preference(UserDto.PreferredContact.PHONE)
                .build();

        followee = UserDto.builder()
                .id(2L)
                .username("User2")
                .email("user2@example.com")
                .phone("+987654321")
                .preference(UserDto.PreferredContact.PHONE)
                .build();

        listener = new FollowerEventListener(
                objectMapper,
                userServiceClient,
                List.of(builder),
                List.of(notifier)
        );
    }

    @Test
    void shouldProcessFollowerEventSuccessfully() {
        when(userServiceClient.getUser(10L)).thenReturn(follower);
        when(userServiceClient.getUser(2L)).thenReturn(followee);
        when(message.getBody()).thenReturn(new byte[0]);
        when(builder.supportsEventType()).thenReturn((Class) FollowerEvent.class);
        when(builder.buildMessage(event, Locale.ENGLISH))
                .thenReturn("Congrats! You've got a new follower User!");
        when(notifier.getPreferredContact()).thenReturn(UserDto.PreferredContact.PHONE);

        listener.onMessage(message, null);

        verify(builder).buildMessage(event, Locale.ENGLISH);
        verify(notifier).send(followee, "Congrats! You've got a new follower User!");
    }

    @Test
    void shouldThrowWhenNoBuilderFound() {
        when(userServiceClient.getUser(10L)).thenReturn(follower);
        when(message.getBody()).thenReturn(new byte[0]);

        listener = new FollowerEventListener(
                objectMapper,
                userServiceClient,
                Collections.emptyList(),
                List.of(notifier)
        );

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                listener.onMessage(message, null)
        );

        assertEquals("No message builder found for event", exception.getMessage());
    }


    @Test
    void shouldThrowWhenDeserializationFails() throws Exception {
        when(objectMapper.readValue(any(byte[].class), eq(FollowerEvent.class)))
                .thenThrow(new IOException("Deserialization error"));

        assertThrows(RuntimeException.class, () ->
                listener.onMessage(message, null)
        );
    }
}

