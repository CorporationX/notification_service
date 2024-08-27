package faang.school.notificationservice.messaging.mentorship;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.mentorship.request.MentorshipOfferedEvent;
import faang.school.notificationservice.exception.feign.UserServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.web.client.RestClientException;

import java.util.Locale;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MentorshipOfferedMessageBuilderTest {
    @Mock
    private MessageSource messageSource;
    @Mock
    private UserServiceClient userServiceClient;
    @InjectMocks
    private MentorshipOfferedMessageBuilder messageBuilder;

    private MentorshipOfferedEvent event;
    private UserDto receiver;

    @BeforeEach
    void setUp() {
        event = new MentorshipOfferedEvent(UUID.randomUUID(),
                1L, 2L, 3L);
        receiver = new UserDto();
        receiver.setId(event.getRequesterId());
        receiver.setUsername("testName");
    }

    @Test
    void buildMessageReturnsMessageForValidEvent() {
        when(userServiceClient.getUser(anyLong())).thenReturn(receiver);
        when(messageSource.getMessage(eq("mentorship.offered"), any(), eq(Locale.US)))
                .thenReturn("Mentorship accepted message");

        String message = messageBuilder.buildMessage(event, Locale.US);

        assertEquals("Mentorship accepted message", message);
    }

    @Test
    void buildMessageThrowsUserServiceException() {
        when(userServiceClient.getUser(anyLong()))
                .thenThrow(new RestClientException("User service error"));

        assertThrows(UserServiceException.class, () -> messageBuilder.buildMessage(event, Locale.US));
    }
}
