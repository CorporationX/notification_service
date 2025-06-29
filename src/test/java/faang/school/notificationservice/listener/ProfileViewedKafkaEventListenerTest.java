package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.ProfileViewedEventDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProfileViewedKafkaEventListenerTest {
    @Mock
    private NotificationService notificationService;

    @Mock
    private MessageBuilder<ProfileViewedEventDto> messageBuilder;

    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private ProfileViewedKafkaEventListener listener;

    @BeforeEach
    void setUp() {

        listener = new ProfileViewedKafkaEventListener(
                List.of(notificationService),
                List.of(messageBuilder),
                userServiceClient
        );
    }

    @Test
    void handleProfileViewedEvent_success() {

        ProfileViewedEventDto event = new ProfileViewedEventDto();
        event.setViewedId(2L);

        ProfileViewedKafkaEventListener spyListener = spy(listener);

        doReturn("Test message").when(spyListener).getMessage(eq(Locale.ENGLISH), eq(event));

        doNothing().when(spyListener).sendNotification(eq(event.getViewedId()), eq("Test message"));

        spyListener.handleProfileViewedEvent(event);

        verify(spyListener).getMessage(eq(Locale.ENGLISH), eq(event));
        verify(spyListener).sendNotification(eq(event.getViewedId()), eq("Test message"));
    }

    @Test
    void handleProfileViewedEvent_logsException() {

        ProfileViewedEventDto event = new ProfileViewedEventDto();
        event.setViewedId(2L);

        ProfileViewedKafkaEventListener spyListener = spy(listener);

        doThrow(new RuntimeException("fail")).when(spyListener).getMessage(Locale.ENGLISH, event);

        spyListener.handleProfileViewedEvent(event);

        verify(spyListener, never()).sendNotification(anyLong(), anyString());
    }
}
