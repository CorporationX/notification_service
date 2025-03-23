package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.context.UserContext;
import faang.school.notificationservice.config.message_source.MessageProperties;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.FollowerEvent;
import faang.school.notificationservice.listener.EventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.time.LocalDateTime;
import java.util.Locale;

import static faang.school.notificationservice.listener.EventType.EVENT_TYPE_SUBSCRIPTION;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FollowerEventMessageBuilderTest {

    @Mock
    private MessageProperties properties;
    @Mock
    private MessageSource messageSource;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private UserContext userContext;
    @InjectMocks
    private FollowerEventMessageBuilder followerEventMessageBuilder;

    private FollowerEvent followerEvent;
    private UserDto userDto;
    private Locale locale;
    private String messageText;

    @BeforeEach
    void setUp() {
        followerEvent = new FollowerEvent(1L, 2L, LocalDateTime.now());
        userDto = new UserDto();
        locale = Locale.ENGLISH;
        messageText = "Congrats! You've got a new follower!";
    }

    @Test
    void testGetEventTypeSuccess() {
        EventType eventType = followerEventMessageBuilder.getEventType();

        assertEquals(EVENT_TYPE_SUBSCRIPTION, eventType);
    }

    @Test
    void testBuildMessageSuccess() {
        when(properties.getPropertyFollower()).thenReturn("follower.new");
        when(userServiceClient.getUser(2L)).thenReturn(userDto);
        when(messageSource.getMessage(
                eq("follower.new"),
                eq(new Object[]{userDto.getUsername()}),
                eq(locale))
        ).thenReturn(messageText);

        String result = followerEventMessageBuilder.buildMessage(followerEvent, locale);

        assertEquals(messageText, result);
        verify(userServiceClient, times(1)).getUser(2L);
        verify(messageSource, times(1)).getMessage(
                eq("follower.new"),
                eq(new Object[]{userDto.getUsername()}),
                eq(locale)
        );
    }
}