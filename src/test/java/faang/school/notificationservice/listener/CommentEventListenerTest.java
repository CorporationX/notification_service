package faang.school.notificationservice.listener;

import faang.school.notificationservice.dto.CommentEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.MessageBuilderNotFoundException;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.NotificationServiceResolver;
import faang.school.notificationservice.service.user.FeignUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentEventListenerTest {

    @Mock
    private MessageBuilder<CommentEvent> messageBuilder;

    @Mock
    private NotificationService notificationService;

    @Mock
    private NotificationServiceResolver serviceResolver;

    @Mock
    private FeignUserService feignUserService;

    @InjectMocks
    private CommentEventListener listener;

    @BeforeEach
    void setUp() {
        listener = new CommentEventListener(
                List.of(messageBuilder),
                serviceResolver,
                feignUserService
        );
    }

    @Test
    @DisplayName("Should handle CommentEvent and send notification")
    public void shouldHandleCommentEvent() {
        CommentEvent event = buildCommentEvent();
        UserDto recipient = buildUserDto();
        String builtMessage = "New comment received";

        doReturn(CommentEvent.class).when(messageBuilder).getInstance();
        when(feignUserService.getById(event.postAuthorId())).thenReturn(recipient);
        when(messageBuilder.buildMessage(eq(event), any(Locale.class))).thenReturn(builtMessage);
        when(serviceResolver.getServiceForPreferredContact(UserDto.PreferredContact.PHONE))
                .thenReturn(notificationService);

        listener.onCommentEvent(event);

        verify(messageBuilder).getInstance();
        verify(feignUserService).getById(event.postAuthorId());
        verify(messageBuilder).buildMessage(eq(event), any(Locale.class));
        verify(serviceResolver).getServiceForPreferredContact(UserDto.PreferredContact.PHONE);
        verify(notificationService).send(recipient, builtMessage);
    }

    @Test
    @DisplayName("Should return correct recipient ID from event")
    public void shouldReturnCorrectRecipientId() {
        CommentEvent event = buildCommentEvent();
        Long recipientId = listener.recipientId(event);
        assertEquals(event.postAuthorId(), recipientId);
    }

    @Test
    @DisplayName("Should throw if no MessageBuilder found")
    public void shouldThrowIfNoBuilderFound() {
        CommentEvent event = buildCommentEvent();
        CommentEventListener listenerWithoutBuilders = new CommentEventListener(
                List.of(), serviceResolver, feignUserService
        );

        MessageBuilderNotFoundException exception = assertThrows(
                MessageBuilderNotFoundException.class,
                () -> listenerWithoutBuilders.onCommentEvent(event)
        );

        assertTrue(exception.getMessage().contains("No MessageBuilder"));
    }

    private CommentEvent buildCommentEvent() {
        return new CommentEvent(
                1L,
                2L,
                3L,
                4L,
                "Sample comment"
        );
    }

    private UserDto buildUserDto() {
        UserDto user = new UserDto();
        user.setId(2L);
        user.setUsername("Test");
        user.setEmail("test@example.com");
        user.setPhone("+1234567890");
        user.setPreference(UserDto.PreferredContact.PHONE);
        return user;
    }
}
