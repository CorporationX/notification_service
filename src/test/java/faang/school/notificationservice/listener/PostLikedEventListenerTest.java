package faang.school.notificationservice.listener;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.kafka.PostLikedNotificationEvent;
import faang.school.notificationservice.listener.like.PostLikedEventListener;
import faang.school.notificationservice.service.notification.handler.PostLikedNotificationEventHandler;
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
class PostLikedEventListenerTest {

    @Mock
    private PostLikedNotificationEventHandler eventHandler;

    @InjectMocks
    private PostLikedEventListener postLikedEventListener;

    @Captor
    private ArgumentCaptor<List<PostLikedNotificationEvent>> eventListCaptor;

    private PostLikedNotificationEvent validEvent;

    @BeforeEach
    void setUp() {
        UserDto userDto = UserDto.builder().id(1L).build();
        validEvent = PostLikedNotificationEvent.builder()
                .owner(userDto)
                .likeId(100L)
                .postId(200L)
                .build();
    }

    @Test
    void testListenPostLikedBatch() {
        PostLikedNotificationEvent invalidEvent = PostLikedNotificationEvent.builder().build(); // no owner
        List<PostLikedNotificationEvent> events = List.of(validEvent, invalidEvent);

        postLikedEventListener.listenPostLikedBatch(events);

        verify(eventHandler, times(1)).saveNotifications(eventListCaptor.capture());
        List<PostLikedNotificationEvent> capturedEvents = eventListCaptor.getValue();

        assertEquals(2, capturedEvents.size());
        assertEquals(validEvent, capturedEvents.get(0));

    }
}