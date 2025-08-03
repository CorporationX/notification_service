package faang.school.notificationservice.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

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
        listener = new ProfileViewedKafkaEventListener(
                List.of(notificationService),
                List.of(messageBuilder),
                kafkaProperties,
                userServiceClient,
                objectMapper
        );
    }

    @Test
    void handleProfileViewedEvent_success() throws Exception {
        ProfileViewedEventDto event = new ProfileViewedEventDto(
                "Mike",
                "Nike",
                2L,
                1L,
                LocalDateTime.now()

        );


        UserDto viewedUser = new UserDto(
                2,
                "Mike",
                "someemail@gmail.com",
                "89191991919",
                UserDto.PreferredContact.EMAIL
        );
        viewedUser.setId(2L);

        String json = "{\"viewerId\":1,\"viewedId\":2}";

        when(kafkaProperties.isUseKafka()).thenReturn(true);
        when(objectMapper.readValue(eq(json), eq(ProfileViewedEventDto.class))).thenReturn(event);
        ProfileViewedKafkaEventListener spyListener = spy(listener);
        doReturn("Test message").when(spyListener).getMessage(any(Locale.class), eq(event));
        when(userServiceClient.getUser(2L)).thenReturn(viewedUser);

        spyListener.listen(json);

        verify(spyListener).getMessage(eq(Locale.ENGLISH), eq(event));
        verify(userServiceClient).getUser(2L);
        verify(spyListener).sendNotification(eq(viewedUser), eq("Test message"));
    }

    @Test
    void handleProfileViewedEvent_logsException() throws Exception {
        String badJson = "{ bad json }";

        when(kafkaProperties.isUseKafka()).thenReturn(true);
        when(objectMapper.readValue(anyString(), eq(ProfileViewedEventDto.class)))
                .thenThrow(new JsonProcessingException("fail") {});

        ProfileViewedKafkaEventListener spyListener = spy(listener);

        spyListener.listen(badJson);

        verify(spyListener, never()).sendNotification(any(), anyString());
    }
}
