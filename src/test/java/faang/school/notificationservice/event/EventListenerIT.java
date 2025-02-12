package faang.school.notificationservice.event;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationServiceHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class EventListenerIT {

    @Autowired
    private EventListenerImpl eventListener;

    @MockBean
    private NotificationServiceHandler notificationServiceHandler;

    @MockBean
    private UserServiceClient userServiceClient;

    @MockBean
    private MessageSource messageSource;

    private static final String EVENT_JSON = "{\"userId\": 1, \"username\": \"test_user\", \"email\": \"test@example.com\"}";

    @Test
    void shouldSendNotificationWithRealMessageSource() {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setUsername("test_user");
        user.setLocale("fr");

        System.out.println("User locale: " + user.getLocale());

        when(userServiceClient.getUser(1L)).thenReturn(user);

        when(messageSource.getMessage(eq("follower.new"), isNull(), eq(Locale.FRENCH)))
                .thenReturn("Félicitations ! Vous avez un nouveau follower !");

        eventListener.processEvent(EVENT_JSON);

        verify(notificationServiceHandler).sendNotification(eq(user), eq("Félicitations ! Vous avez un nouveau follower !"));
    }
}