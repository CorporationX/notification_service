package faang.school.notificationservice.event;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.listener.LikeEventListener;
import faang.school.notificationservice.service.NotificationServiceHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class LikeEventListenerIT {

    @Autowired
    private LikeEventListener likeEventListener;

    @MockBean
    private NotificationServiceHandler notificationServiceHandler;

    @MockBean
    private UserServiceClient userServiceClient;

    @MockBean
    private MessageSource messageSource;

    private static final String EVENT_JSON = "{\"postId\": 1, \"userId\": 2, \"authorId\": 3}";

    @Test
    void shouldSendNotificationWithRealMessageSource() {
        UserDto user = new UserDto();
        user.setId(3L);
        user.setUsername("test_user");
        user.setLocale("es");

        when(userServiceClient.getUser(3L)).thenReturn(user);
        when(messageSource.getMessage(eq("like.notification"), any(), eq(Locale.forLanguageTag("es"))))
                .thenReturn("¡El usuario 2 le gustó tu publicación 1!");

        likeEventListener.handleLikeEvent(EVENT_JSON);

        verify(notificationServiceHandler).sendNotification(eq(user), eq("¡El usuario 2 le gustó tu publicación 1!"));
    }
}