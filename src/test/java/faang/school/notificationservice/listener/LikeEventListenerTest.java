package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.LikePostEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.LikeMessageBuilder;
import faang.school.notificationservice.service.NotificationServiceSelector;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LikeEventListenerTest {
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private LikeMessageBuilder likeMessageBuilder;
    @Mock
    private NotificationServiceSelector notificationServiceSelector;

    @InjectMocks
    private LikeEventListener likeEventListener;

    private static final String MESSAGE = "User123 liked your post! ❤️";
    private final long postAuthorId = 1L;
    private final long likerId = 2L;
    private final long postId = 3L;
    private final LikePostEvent likePostEvent = new LikePostEvent(postAuthorId, likerId, postId);
    private final UserDto userDto = new UserDto();

    @Test
    @DisplayName("Обработка лайка поста - успешная отправка уведомления автору")
    public void givenValidLikeEvent_WhenHandleMessage_ThenNotificationSent() {
        when(userServiceClient.getUser(postAuthorId)).thenReturn(userDto);
        when(likeMessageBuilder.buildMessage(likePostEvent, Locale.ENGLISH)).thenReturn(MESSAGE);

        likeEventListener.handleMessage(likePostEvent);

        verify(userServiceClient, times(1)).getUser(postAuthorId);
        verify(likeMessageBuilder, times(1)).buildMessage(likePostEvent, Locale.ENGLISH);
        verify(notificationServiceSelector, times(1)).notifyUser(userDto, MESSAGE);
    }
}