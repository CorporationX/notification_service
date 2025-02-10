package faang.school.notificationservice.event;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.listener.LikeEventListener;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class LikeEventListenerTest {

    private NotificationService notificationService;
    private UserServiceClient userServiceClient;
    private LikeEventListener likeEventListener;

    @BeforeEach
    void setUp() {
        notificationService = mock(NotificationService.class);
        userServiceClient = mock(UserServiceClient.class);
        likeEventListener = new LikeEventListener(notificationService, userServiceClient);
    }

    @Test
    void testHandleLikeEvent() {
        LikeEvent event = new LikeEvent(1L, 2L, 3L);
        UserDto user = new UserDto();
        user.setEmail("test@example.com");
        user.setPreference(UserDto.PreferredContact.EMAIL);

        when(userServiceClient.getUser(event.getAuthorId())).thenReturn(user);

        likeEventListener.handleLikeEvent(event);

        verify(notificationService, times(1)).send(eq(user), anyString());
    }
}