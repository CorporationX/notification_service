package faang.school.notificationservice.listener;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.kafka.CommentLikedNotificationEvent;
import faang.school.notificationservice.listener.like.CommentLikedEventListener;
import faang.school.notificationservice.service.notification.handler.CommentLikedNotificationEventHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommentLikedEventListenerTest {

    @Mock
    private CommentLikedNotificationEventHandler eventHandler;

    @InjectMocks
    private CommentLikedEventListener commentLikedEventListener;

    @Captor
    private ArgumentCaptor<List<CommentLikedNotificationEvent>> eventListCaptor;

    private CommentLikedNotificationEvent validEvent;

    @BeforeEach
    void setUp() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .username("test_user")
                .email("test@gmail.com")
                .phone("123456789")
                .build();

        validEvent = CommentLikedNotificationEvent.builder()
                .commentId(202L)
                .likerUsername("some_user")
                .owner(userDto)
                .shortContent("test comment")
                .build();
    }

    @Test
    void testListenCommentLikedBatch() {
        CommentLikedNotificationEvent invalidEvent = CommentLikedNotificationEvent.builder().build();
        List<CommentLikedNotificationEvent> events = List.of(validEvent, invalidEvent);

        commentLikedEventListener.processEventsBatch(events);

        verify(eventHandler, times(1)).saveNotifications(eventListCaptor.capture());
        List<CommentLikedNotificationEvent> capturedEvents = eventListCaptor.getValue();

        assertEquals(1, capturedEvents.size());
        assertEquals(validEvent, capturedEvents.get(0));
    }
}