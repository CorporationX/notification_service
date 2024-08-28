package faang.school.notificationservice.messaging.mentorship;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.mentorship.request.MentorshipAcceptedEvent;
import faang.school.notificationservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MentorshipAcceptedMessageBuilderTest {

    @Mock
    private MessageSource messageSource;
    @Mock
    private UserService userService;
    @InjectMocks
    private MentorshipAcceptedMessageBuilder messageBuilder;

    private MentorshipAcceptedEvent event;
    private UserDto receiver;

    @BeforeEach
    void setUp() {
        event = new MentorshipAcceptedEvent(UUID.randomUUID(),
                123L, 456L, 789L);
        receiver = new UserDto();
        receiver.setId(event.getRequesterId());
        receiver.setUsername("testUser");
    }

    @Test
    void buildMessageReturnsMessageForValidEvent() {
        when(userService.fetchUser(anyLong())).thenReturn(receiver);
        when(messageSource.getMessage(eq("mentorship.accepted"), any(), eq(Locale.US)))
                .thenReturn("Mentorship accepted message");

        String message = messageBuilder.buildMessage(event, Locale.US);

        assertEquals("Mentorship accepted message", message);
    }

    @Test
    void buildMessageThrowsUserServiceException() {
        when(userService.fetchUser(anyLong())).thenThrow(new RuntimeException("User service error"));

        assertThrows(RuntimeException.class, () -> messageBuilder.buildMessage(event, Locale.US));
    }
}