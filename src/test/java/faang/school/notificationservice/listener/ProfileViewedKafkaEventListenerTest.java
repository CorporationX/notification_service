package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.kafka.KafkaProperties;
import faang.school.notificationservice.dto.ProfileViewedEventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.listener.kafka.ProfileViewedKafkaEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProfileViewedKafkaEventListenerTest {

    @Mock
    private NotificationService notificationService;

    @Mock
    private MessageBuilder<ProfileViewedEventDto> messageBuilder;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private KafkaProperties kafkaProperties;

    @Mock
    private ObjectMapper objectMapper;

    private ProfileViewedKafkaEventListener listener;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        listener = spy(new ProfileViewedKafkaEventListener(
                List.of(notificationService),
                List.of(messageBuilder),
                kafkaProperties,
                userServiceClient,
                objectMapper
        ));
    }

    @Test
    void handleProfileViewedEvent_success() throws Exception {
        ProfileViewedEventDto event = new ProfileViewedEventDto();
        event.setViewedId(2L);
        event.setViewerId(1L);

        UserDto viewedUser = new UserDto();
        viewedUser.setId(2L);

        String json = "{\"viewerId\":1,\"viewedId\":2}";

        when(kafkaProperties.isUseKafka()).thenReturn(true);
        when(objectMapper.readValue(eq(json), eq(ProfileViewedEventDto.class))).thenReturn(event);
        doReturn("Test message").when(listener).getMessage(any(Locale.class), any(ProfileViewedEventDto.class));
        when(userServiceClient.getUser(2L)).thenReturn(viewedUser);
        doNothing().when(listener).sendNotification(eq(viewedUser), eq("Test message"));


        listener.listen(json);


        verify(listener).getMessage(eq(Locale.ENGLISH), any(ProfileViewedEventDto.class));
        verify(userServiceClient).getUser(2L);
        verify(listener).sendNotification(eq(viewedUser), eq("Test message"));
    }

    @Test
    void handleProfileViewedEvent_logsException() throws Exception {
        String badJson = "{ bad json }";

        when(kafkaProperties.isUseKafka()).thenReturn(true);
        when(objectMapper.readValue(anyString(), eq(ProfileViewedEventDto.class)))
                .thenThrow(new RuntimeException("fail"));

        assertThrows(RuntimeException.class, () -> listener.listen(badJson));

        verify(listener, never()).sendNotification(any(), anyString());
    }
}
