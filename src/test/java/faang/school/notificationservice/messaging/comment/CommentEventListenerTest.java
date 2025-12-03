package faang.school.notificationservice.messaging.comment;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.CommentEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentEventListenerTest {

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private MessageBuilder<CommentEvent> commentMessageBuilder;

    @Mock
    private NotificationService notificationService;

    private CommentEventListener commentEventListener;

    private List<MessageBuilder<?>> messageBuilders;
    private List<NotificationService> notificationServices;

    private CommentEvent commentEvent;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        messageBuilders = new ArrayList<>();
        notificationServices = new ArrayList<>();

        commentEvent = CommentEvent.builder()
                .commentId(1L)
                .commentAuthorId(10L)
                .postAuthorId(20L)
                .postId(100L)
                .commentText("Test comment")
                .build();

        userDto = new UserDto();
        userDto.setId(20L);
        userDto.setUsername("testuser");
        userDto.setEmail("test@example.com");
        userDto.setPreference(UserDto.PreferredContact.EMAIL);
        userDto.setLocale(Locale.ENGLISH);

        messageBuilders.add(commentMessageBuilder);
        notificationServices.add(notificationService);

        commentEventListener = new CommentEventListener(
                userServiceClient,
                messageBuilders,
                notificationServices
        );
    }

    @Test
    void testOnCommentEvent_Success() {
        doReturn(CommentEvent.class).when(commentMessageBuilder).getInstance();
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);
        when(userServiceClient.getUser(20L)).thenReturn(userDto);
        when(commentMessageBuilder.buildMessage(eq(commentEvent), eq(Locale.ENGLISH)))
                .thenReturn("Notification message");

        commentEventListener.onCommentEvent(commentEvent);

        verify(userServiceClient, times(1)).getUser(20L);
        verify(commentMessageBuilder, times(1)).buildMessage(eq(commentEvent), eq(Locale.ENGLISH));
        verify(notificationService, times(1)).send(eq(userDto), eq("Notification message"));
    }

    @Test
    void testOnCommentEvent_UserNotFound() {
        when(userServiceClient.getUser(20L)).thenReturn(null);

        commentEventListener.onCommentEvent(commentEvent);

        verify(userServiceClient, times(1)).getUser(20L);
        verify(commentMessageBuilder, never()).buildMessage(any(), any());
        verify(notificationService, never()).send(any(), anyString());
    }

    @Test
    void testOnCommentEvent_MessageBuilderNotFound() {
        messageBuilders.clear();
        when(userServiceClient.getUser(20L)).thenReturn(userDto);

        commentEventListener.onCommentEvent(commentEvent);

        verify(userServiceClient, times(1)).getUser(20L);
        verify(commentMessageBuilder, never()).buildMessage(any(), any());
        verify(notificationService, never()).send(any(), anyString());
    }

    @Test
    void testOnCommentEvent_NotificationServiceNotFound() {
        notificationServices.clear();
        doReturn(CommentEvent.class).when(commentMessageBuilder).getInstance();
        when(userServiceClient.getUser(20L)).thenReturn(userDto);
        when(commentMessageBuilder.buildMessage(eq(commentEvent), eq(Locale.ENGLISH)))
                .thenReturn("Notification message");

        commentEventListener.onCommentEvent(commentEvent);

        verify(userServiceClient, times(1)).getUser(20L);
        verify(commentMessageBuilder, times(1)).buildMessage(eq(commentEvent), eq(Locale.ENGLISH));
        verify(notificationService, never()).send(any(), anyString());
    }

    @Test
    void testOnCommentEvent_WithNullLocale_UsesDefaultLocale() {
        userDto.setLocale(null);
        doReturn(CommentEvent.class).when(commentMessageBuilder).getInstance();
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);
        when(userServiceClient.getUser(20L)).thenReturn(userDto);
        when(commentMessageBuilder.buildMessage(eq(commentEvent), any(Locale.class)))
                .thenReturn("Notification message");

        commentEventListener.onCommentEvent(commentEvent);

        verify(userServiceClient, times(1)).getUser(20L);
        verify(commentMessageBuilder, times(1)).buildMessage(eq(commentEvent), any(Locale.class));
        verify(notificationService, times(1)).send(eq(userDto), eq("Notification message"));
    }

    @Test
    void testOnCommentEvent_WithDifferentPreferredContact() {
        userDto.setPreference(UserDto.PreferredContact.PHONE);
        NotificationService phoneService = mock(NotificationService.class);
        when(phoneService.getPreferredContact()).thenReturn(UserDto.PreferredContact.PHONE);
        notificationServices.clear();
        notificationServices.add(phoneService);

        doReturn(CommentEvent.class).when(commentMessageBuilder).getInstance();
        when(userServiceClient.getUser(20L)).thenReturn(userDto);
        when(commentMessageBuilder.buildMessage(eq(commentEvent), eq(Locale.ENGLISH)))
                .thenReturn("Notification message");

        commentEventListener.onCommentEvent(commentEvent);

        verify(userServiceClient, times(1)).getUser(20L);
        verify(commentMessageBuilder, times(1)).buildMessage(eq(commentEvent), eq(Locale.ENGLISH));
        verify(phoneService, times(1)).send(eq(userDto), eq("Notification message"));
        verify(notificationService, never()).send(any(), anyString());
    }

    @Test
    void testOnCommentEvent_ExceptionHandling() {
        when(userServiceClient.getUser(20L)).thenThrow(new RuntimeException("Service unavailable"));

        assertThatCode(() -> commentEventListener.onCommentEvent(commentEvent))
                .doesNotThrowAnyException();

        verify(userServiceClient, times(1)).getUser(20L);
        verify(commentMessageBuilder, never()).buildMessage(any(), any());
        verify(notificationService, never()).send(any(), anyString());
    }

    @Test
    void testOnCommentEvent_ExceptionInMessageBuilder() {
        doReturn(CommentEvent.class).when(commentMessageBuilder).getInstance();
        when(userServiceClient.getUser(20L)).thenReturn(userDto);
        when(commentMessageBuilder.buildMessage(eq(commentEvent), eq(Locale.ENGLISH)))
                .thenThrow(new RuntimeException("Message building failed"));

        assertThatCode(() -> commentEventListener.onCommentEvent(commentEvent))
                .doesNotThrowAnyException();

        verify(userServiceClient, times(1)).getUser(20L);
        verify(commentMessageBuilder, times(1)).buildMessage(eq(commentEvent), eq(Locale.ENGLISH));
        verify(notificationService, never()).send(any(), anyString());
    }

    @Test
    void testOnCommentEvent_ExceptionInNotificationService() {
        doReturn(CommentEvent.class).when(commentMessageBuilder).getInstance();
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);
        when(userServiceClient.getUser(20L)).thenReturn(userDto);
        when(commentMessageBuilder.buildMessage(eq(commentEvent), eq(Locale.ENGLISH)))
                .thenReturn("Notification message");
        doThrow(new RuntimeException("Send failed")).when(notificationService).send(any(), anyString());

        assertThatCode(() -> commentEventListener.onCommentEvent(commentEvent))
                .doesNotThrowAnyException();

        verify(userServiceClient, times(1)).getUser(20L);
        verify(commentMessageBuilder, times(1)).buildMessage(eq(commentEvent), eq(Locale.ENGLISH));
        verify(notificationService, times(1)).send(eq(userDto), eq("Notification message"));
    }
}

