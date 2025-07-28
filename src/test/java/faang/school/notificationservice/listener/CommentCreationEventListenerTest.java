package faang.school.notificationservice.listener;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.kafka.CommentCreationNotificationEvent;
import faang.school.notificationservice.listener.comment.CommentCreationEventListener;
import faang.school.notificationservice.listener.data.UserData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CommentCreationEventListenerTest {

    @Spy
    @InjectMocks
    private CommentCreationEventListener commentCreationEventListener;

    private static final String COMMENT_AUTHOR_USERNAME = "username";
    private static final String SHORT_CONTENT = "content";

    @Test
    public void testListenCommentCreation_whenValidEvent() {
        ArgumentCaptor<CommentCreationNotificationEvent> eventNotificationCaptor = ArgumentCaptor.forClass(CommentCreationNotificationEvent.class);
        ArgumentCaptor<UserDto> userCaptor = ArgumentCaptor.forClass(UserDto.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        CommentCreationNotificationEvent commentCreationEvent = CommentCreationNotificationEvent.builder()
                .owner(UserData.CORRECT_USER_DTO)
                .commentAuthorUserName(COMMENT_AUTHOR_USERNAME)
                .shortContent(SHORT_CONTENT)
                .build();

        doNothing().when(commentCreationEventListener).sendNotification(any(CommentCreationNotificationEvent.class));
        doReturn("Mocked message").when(commentCreationEventListener).getMessage(any(CommentCreationNotificationEvent.class));

        commentCreationEventListener.listenCommentCreation(commentCreationEvent);

        verify(commentCreationEventListener, times(1)).sendNotification(eventNotificationCaptor.capture());

        assertEquals(userCaptor.getValue().getId(), eventNotificationCaptor.getValue().owner().getId());
        assertEquals("Mocked message", messageCaptor.getValue());

    }

    @Test
    public void testListenCommentCreation_whenInvalidEvent() {
        ArgumentCaptor<CommentCreationNotificationEvent> eventNotificationCaptor = ArgumentCaptor.forClass(CommentCreationNotificationEvent.class);
        CommentCreationNotificationEvent commentCreationEvent = CommentCreationNotificationEvent.builder()
                .owner(UserData.CORRECT_USER_DTO)
                .commentAuthorUserName(null)
                .shortContent(null)
                .build();

        commentCreationEventListener.listenCommentCreation(commentCreationEvent);

        verify(commentCreationEventListener, times(1)).sendNotification(eventNotificationCaptor.capture());
        verify(commentCreationEventListener, never()).sendNotification(any(CommentCreationNotificationEvent.class));

        assertEquals(UserData.CORRECT_USER_DTO.getId(), eventNotificationCaptor.getValue().owner().getId());
    }

    @Test
    public void testIsEventValid_whenValidEvent() {
        CommentCreationNotificationEvent commentCreationEvent = CommentCreationNotificationEvent.builder()
                .owner(UserData.CORRECT_USER_DTO)
                .commentAuthorUserName(COMMENT_AUTHOR_USERNAME)
                .shortContent(SHORT_CONTENT)
                .build();

        assertTrue(commentCreationEventListener.isEventValid(commentCreationEvent));
    }

    @Test
    public void testIsEventValid_whenCommentAuthorUserNameIsNull() {
        CommentCreationNotificationEvent commentCreationEvent = CommentCreationNotificationEvent.builder()
                .owner(UserData.CORRECT_USER_DTO)
                .commentAuthorUserName(null)
                .shortContent(SHORT_CONTENT)
                .build();

        assertFalse(commentCreationEventListener.isEventValid(commentCreationEvent));
    }

    @Test
    public void testIsEventValid_whenShortContentIsNull() {
        CommentCreationNotificationEvent commentCreationEvent = CommentCreationNotificationEvent.builder()
                .owner(UserData.CORRECT_USER_DTO)
                .commentAuthorUserName(COMMENT_AUTHOR_USERNAME)
                .shortContent(null)
                .build();

        assertFalse(commentCreationEventListener.isEventValid(commentCreationEvent));
    }

    @Test
    public void testIsEventValid_whenOwnerIsNotCorrect() {
        CommentCreationNotificationEvent commentCreationEvent = CommentCreationNotificationEvent.builder()
                .owner(UserData.NO_ID_USER_DTO)
                .commentAuthorUserName(COMMENT_AUTHOR_USERNAME)
                .shortContent(SHORT_CONTENT)
                .build();

        assertFalse(commentCreationEventListener.isEventValid(commentCreationEvent));
    }

    @Test
    public void testIsEventValid_whenOwnerIsNull() {
        CommentCreationNotificationEvent commentCreationEvent = CommentCreationNotificationEvent.builder()
                .owner(null)
                .commentAuthorUserName(COMMENT_AUTHOR_USERNAME)
                .shortContent(SHORT_CONTENT)
                .build();

        assertFalse(commentCreationEventListener.isEventValid(commentCreationEvent));
    }

}
