package faang.school.notificationservice.listener;

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
        ArgumentCaptor<CommentCreationNotificationEvent> eventCaptor = ArgumentCaptor.forClass(CommentCreationNotificationEvent.class);
        CommentCreationNotificationEvent commentEvent = CommentCreationNotificationEvent.builder()
                .owner(UserData.CORRECT_USER_DTO)
                .commentAuthorUserName(COMMENT_AUTHOR_USERNAME)
                .shortContent(SHORT_CONTENT)
                .build();

        doNothing().when(commentCreationEventListener).sendNotification(any(CommentCreationNotificationEvent.class));

        commentCreationEventListener.listenCommentCreation(commentEvent);

        verify(commentCreationEventListener, times(1)).sendNotification(eventCaptor.capture());

        CommentCreationNotificationEvent capturedEvent = eventCaptor.getValue();
        assertEquals(UserData.CORRECT_USER_DTO, capturedEvent.getOwner());
        assertEquals(COMMENT_AUTHOR_USERNAME, capturedEvent.getCommentAuthorUserName());
        assertEquals(SHORT_CONTENT, capturedEvent.getShortContent());
    }

    @Test
    public void testIsEventValid_whenValidEvent() {
        CommentCreationNotificationEvent commentEvent = CommentCreationNotificationEvent.builder()
                .owner(UserData.CORRECT_USER_DTO)
                .commentAuthorUserName(COMMENT_AUTHOR_USERNAME)
                .shortContent(SHORT_CONTENT)
                .build();

        assertTrue(commentCreationEventListener.isEventValid(commentEvent));
    }

    @Test
    public void testIsEventValid_whenCommentAuthorUserNameIsNull() {
        CommentCreationNotificationEvent commentEvent = CommentCreationNotificationEvent.builder()
                .owner(UserData.CORRECT_USER_DTO)
                .commentAuthorUserName(null)
                .shortContent(SHORT_CONTENT)
                .build();

        assertFalse(commentCreationEventListener.isEventValid(commentEvent));
    }

    @Test
    public void testIsEventValid_whenShortContentIsNull() {
        CommentCreationNotificationEvent commentEvent = CommentCreationNotificationEvent.builder()
                .owner(UserData.CORRECT_USER_DTO)
                .commentAuthorUserName(COMMENT_AUTHOR_USERNAME)
                .shortContent(null)
                .build();

        assertFalse(commentCreationEventListener.isEventValid(commentEvent));
    }

    @Test
    public void testIsEventValid_whenOwnerIsNotCorrect() {
        CommentCreationNotificationEvent commentEvent = CommentCreationNotificationEvent.builder()
                .owner(UserData.NO_ID_USER_DTO)
                .commentAuthorUserName(COMMENT_AUTHOR_USERNAME)
                .shortContent(SHORT_CONTENT)
                .build();

        assertFalse(commentCreationEventListener.isEventValid(commentEvent));
    }

    @Test
    public void testIsEventValid_whenOwnerIsNull() {
        CommentCreationNotificationEvent commentEvent = CommentCreationNotificationEvent.builder()
                .owner(null)
                .commentAuthorUserName(COMMENT_AUTHOR_USERNAME)
                .shortContent(SHORT_CONTENT)
                .build();

        assertFalse(commentCreationEventListener.isEventValid(commentEvent));
    }
}