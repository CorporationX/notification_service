package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEvent;
import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.time.LocalDateTime;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FollowerEventBuilderTest {

    @Mock
    private MessageSource messageSource;

    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private FollowerEventBuilder builder;

    private FollowerEvent event;
    private UserDto follower;

    @BeforeEach
    void setUp() {
        event = FollowerEvent.builder()
                .followerId(10L)
                .followeeId(2L)
                .timestamp(LocalDateTime.now())
                .build();

        follower = UserDto.builder()
                .id(10L)
                .username("User")
                .email("user@example.org")
                .phone("+123456789")
                .preference(UserDto.PreferredContact.PHONE)
                .build();
    }

    @Test
    void shouldReturnTrueForFollowerEvent() {
        assertTrue(builder.supportsEventType().isAssignableFrom(FollowerEvent.class));
    }

    @Test
    void shouldReturnFalseForOtherEvent() {
        assertFalse(builder.supportsEventType().isAssignableFrom(Object.class));
    }

    @Test
    void shouldBuildMessageSuccessfully() {
        when(userServiceClient.getUser(10L)).thenReturn(follower);
        when(messageSource.getMessage(
                eq("follower.new"),
                aryEq(new Object[]{"User"}),
                eq(Locale.ENGLISH)))
                .thenReturn("Congrats! You've got a new follower User!");

        String result = builder.buildMessage(event, Locale.ENGLISH);

        assertEquals("Congrats! You've got a new follower User!", result);
    }

    @Test
    void shouldUseDefaultLocaleWhenLocaleIsNull() {
        when(userServiceClient.getUser(10L)).thenReturn(follower);
        when(messageSource.getMessage(
                eq("follower.new"),
                aryEq(new Object[]{"User"}),
                eq(Locale.ENGLISH)))
                .thenReturn("Congrats! You've got a new follower User!");

        String msg = builder.buildMessage(event, null);
        assertEquals("Congrats! You've got a new follower User!", msg);
    }

    @Test
    void shouldThrowExceptionWhenMessageNotFound() {
        when(userServiceClient.getUser(10L)).thenReturn(follower);
        when(messageSource.getMessage(eq("follower.new"), aryEq(new Object[]{"User"}), eq(Locale.ENGLISH)))
                .thenThrow(new NoSuchMessageException("follower.new", Locale.ENGLISH));

        assertThrows(RuntimeException.class, () -> builder.buildMessage(event, Locale.ENGLISH));
    }
}
