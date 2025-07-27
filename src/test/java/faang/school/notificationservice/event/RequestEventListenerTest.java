package faang.school.notificationservice.event;

import faang.school.notificationservice.client.FeignUserServiceAdapter;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.Acknowledgment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RequestEventListenerTest {

    @Mock
    private MessageBuilder<RequestEvent> messageBuilder;

    private NotificationService emailService;

    @Mock
    private FeignUserServiceAdapter feignAdapter;

    private RequestEventListener listener;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        emailService = mock(NotificationService.class);
        when(emailService.getPreferredContact())
                .thenReturn(UserDto.PreferredContact.EMAIL);

        List<NotificationService> notificationServices = List.of(emailService);

        listener = new RequestEventListener(
                messageBuilder,
                notificationServices,
                feignAdapter
        );
        listener.init();
    }

    @Test
    void onRequestEvent_HappyPath_SendsNotificationAndAcks() {
        UUID key = UUID.randomUUID();
        RequestEvent event = new RequestEvent(
                key,
                42L,
                "TRANSFER",
                "COMPLETED",
                "All good",
                "request-status-changed",
                LocalDateTime.now()
        );

        UserDto user = new UserDto();
        user.setId(42L);
        user.setUsername("alice");
        user.setEmail("alice@example.com");
        user.setLocale(Locale.ENGLISH);
        user.setPreference(UserDto.PreferredContact.EMAIL);

        when(feignAdapter.fetchUserDtosViaFeign(eq(42L), anyString(), anyLong()))
                .thenReturn(Optional.of(user));

        when(messageBuilder.buildMessage(eq(event), eq(Locale.ENGLISH)))
                .thenReturn("Your request is completed");

        listener.onRequestEvent(event);

        verify(emailService).send(
                argThat(u -> u.equals(user)),
                argThat(msg -> msg.equals("Your request is completed"))
        );

    }
}
