package faang.school.notificationservice.listener.profile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.entity.PreferredContact;
import faang.school.notificationservice.event.profile.ProfileViewEvent;
import faang.school.notificationservice.exception.ListenerException;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.apache.kafka.common.errors.SerializationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProfileViewListenerTest {

    @InjectMocks
    private ProfileViewListener profileViewListener;

    @Mock
    private MessageBuilder<ProfileViewEvent> messageBuilder;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private NotificationService notificationService;

    private final String validEventJson = "{\"userId\":1, \"viewerId\":2, \"viewedAt\":\"2024-06-29T12:34:56\"}";
    private ProfileViewEvent validEvent;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        MockitoAnnotations.openMocks(this);
        validEvent = ProfileViewEvent.builder()
                .userId(1L)
                .viewerId(2L)
                .viewedAt(LocalDateTime.now())
                .build();

        when(objectMapper.readValue(validEventJson, ProfileViewEvent.class)).thenReturn(validEvent);
        when(messageBuilder.buildMessage(validEvent, Locale.ENGLISH)).thenReturn("Test message");

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setPreferredContact(PreferredContact.EMAIL);
        when(userServiceClient.getUser(1L)).thenReturn(userDto);

        List<NotificationService> notificationServices = Collections.singletonList(notificationService);
        when(notificationService.getPreferredContact()).thenReturn(PreferredContact.EMAIL);

        try {
            Field field = ProfileViewListener.class.getSuperclass().getDeclaredField("notificationServices");
            field.setAccessible(true);
            field.set(profileViewListener, notificationServices);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void listen_shouldProcessValidEvent() {
        profileViewListener.listen(validEventJson);
        verify(notificationService, times(1)).send(any(UserDto.class), eq("Test message"));
    }

    @Test
    void listen_shouldNotProcessWhenViewerIsUser() throws JsonProcessingException {
        validEvent = ProfileViewEvent.builder()
                .userId(1L)
                .viewerId(1L)
                .viewedAt(LocalDateTime.now())
                .build();
        when(objectMapper.readValue(validEventJson, ProfileViewEvent.class)).thenReturn(validEvent);

        profileViewListener.listen(validEventJson);
        verify(notificationService, times(0)).send(any(UserDto.class), anyString());
    }

    @Test
    void listen_shouldThrowSerializationExceptionOnJsonProcessingException() throws JsonProcessingException {
        when(objectMapper.readValue(validEventJson, ProfileViewEvent.class)).thenThrow(JsonProcessingException.class);

        assertThrows(SerializationException.class, () -> profileViewListener.listen(validEventJson));
    }

    @Test
    void listen_shouldThrowListenerExceptionOnUnexpectedException() {
        when(userServiceClient.getUser(1L)).thenThrow(RuntimeException.class);

        assertThrows(ListenerException.class, () -> profileViewListener.listen(validEventJson));
    }

    @Test
    void listen_shouldLogErrorOnEmptyEvent() {
        String emptyEvent = " ";
        profileViewListener.listen(emptyEvent);
        verify(notificationService, times(0)).send(any(UserDto.class), anyString());
    }
}
