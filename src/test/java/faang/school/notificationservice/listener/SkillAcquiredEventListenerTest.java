package faang.school.notificationservice.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.SkillAcquiredEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.messaging.SkillMessageBuilder;
import faang.school.notificationservice.service.EmailService;
import faang.school.notificationservice.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SkillAcquiredEventListenerTest {
    @InjectMocks
    private SkillAcquiredEventListener skillAcquiredEventListener;
    @Spy
    private ObjectMapper objectMapper;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private EmailService emailService;
    @Mock
    private MessageBuilder<SkillAcquiredEvent> messageBuilder;
    @Mock
    private Message message;
    private SkillAcquiredEvent acquiredEvent;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        List<NotificationService> notifications = new ArrayList<>(List.of(emailService));
        skillAcquiredEventListener = new SkillAcquiredEventListener(objectMapper, userServiceClient, notifications, messageBuilder);
        acquiredEvent = new SkillAcquiredEvent();
        userDto = new UserDto();
        userDto.setId(13);
        userDto.setLocale(Locale.UK);
    }

    @Test
    void testMapMessage() throws JsonProcessingException {
        acquiredEvent.setUserId(12345);
        acquiredEvent.setSkillId(67890);

        String json = objectMapper.writeValueAsString(acquiredEvent);

        when(message.getBody()).thenReturn(json.getBytes());

        SkillAcquiredEvent event = skillAcquiredEventListener.mapMessage(message, SkillAcquiredEvent.class);
        assertEquals(event.getSkillId(), 67890);
        assertEquals(event.getUserId(), 12345);
    }

    @Test
    void testMapMessageThrowsException() {
        when(message.getBody()).thenReturn("".getBytes());

        assertThrows(IllegalArgumentException.class,
                () -> skillAcquiredEventListener.mapMessage(message, SkillAcquiredEvent.class));

    }

    @Test
    void testGetMessage() {
        when(userServiceClient.getUser(13)).thenReturn(userDto);

        skillAcquiredEventListener.getMessage(acquiredEvent, 13);

        verify(messageBuilder).buildMessage(acquiredEvent, userDto.getLocale());
    }

    @Test
    void testSendMessage() {
        userDto.setPreference(UserDto.PreferredContact.EMAIL);

        when(userServiceClient.getUser(13)).thenReturn(userDto);
        when(emailService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        skillAcquiredEventListener.sendMessage(13, "message");

        verify(emailService).send(userDto, "message");
    }

    @Test
    void testSendMessageThrownException() {
        userDto.setPreference(UserDto.PreferredContact.EMAIL);

        when(userServiceClient.getUser(13)).thenReturn(userDto);
        when(emailService.getPreferredContact()).thenReturn(UserDto.PreferredContact.SMS);

        assertThrows(EntityNotFoundException.class,
                () -> skillAcquiredEventListener.sendMessage(13, "message"));

    }

    @Test
    void testOnMessage() throws JsonProcessingException {
        String json = "{\"userId\": 12345, \"skillId\": 67890}";
        acquiredEvent.setUserId(12345);
        acquiredEvent.setSkillId(67890);

        when(message.getBody()).thenReturn(json.getBytes());
        when(objectMapper.readValue(json, SkillAcquiredEvent.class)).thenReturn(acquiredEvent);
        when(userServiceClient.getUser(12345)).thenReturn(new UserDto());

        UserDto userDto = new UserDto();
        userDto.setLocale(Locale.UK);
        when(userServiceClient.getUser(12345)).thenReturn(userDto);
        when(messageBuilder.buildMessage(acquiredEvent, userDto.getLocale())).thenReturn("Test message");

        skillAcquiredEventListener.onMessage(message, new byte[0]);

        verify(objectMapper,times(2)).readValue(json, SkillAcquiredEvent.class);
        verify(userServiceClient,times(2)).getUser(12345);
        verify(messageBuilder).buildMessage(acquiredEvent, userDto.getLocale());
        verify(emailService).send(ArgumentMatchers.any(UserDto.class), ArgumentMatchers.eq("Test message"));
    }
}