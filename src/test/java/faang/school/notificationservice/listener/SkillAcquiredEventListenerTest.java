package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.event.SkillAcquiredEvent;
import faang.school.notificationservice.exception.EventProcessingException;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.messaging.SkillAcquiredMessageBuilder;
import faang.school.notificationservice.model.dto.SkillDto;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SkillAcquiredEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private NotificationService notificationService;

    @Mock
    private MessageSource messageSource;

    @Mock
    private Message message;

    private SkillAcquiredEventListener listener;

    @BeforeEach
    public void setUp() {
        List<NotificationService> notificationServices = Collections.singletonList(notificationService);
        List<MessageBuilder<?>> messageBuilders = Collections.singletonList(
                new SkillAcquiredMessageBuilder(userServiceClient, messageSource));

        listener = new SkillAcquiredEventListener(objectMapper, userServiceClient,
                notificationServices, messageBuilders);
    }

    @Test
    @DisplayName("Should successfully process SkillAcquiredEvent and send notification")
    public void testOnMessage_Success() throws Exception {
        SkillAcquiredEvent event = new SkillAcquiredEvent();
        event.setUserId(1L);
        event.setSkillId(2L);

        byte[] messageBody = objectMapper.writeValueAsBytes(event);
        when(message.getBody()).thenReturn(messageBody);
        when(objectMapper.readValue(messageBody, SkillAcquiredEvent.class)).thenReturn(event);

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("beneficiary");

        SkillDto skillDto = new SkillDto();
        skillDto.setId(2L);
        skillDto.setTitle("FAANG");

        List<SkillDto> skillDtos = Collections.singletonList(skillDto);

        when(userServiceClient.getUser(1L)).thenReturn(userDto);
        when(userServiceClient.getUserSkills(1L)).thenReturn(skillDtos);
        when(messageSource.getMessage(eq("skill_acquired"), any(), eq(Locale.UK))).thenReturn("Notification message");

        listener.onMessage(message, null);

        verify(notificationService, times(1)).send(eq(userDto), eq("Notification message"));
    }

    @Test
    @DisplayName("Should throw EventProcessingException when message parsing fails")
    public void testOnMessage_EventProcessingException() throws Exception {
        byte[] messageBody = new byte[0];
        when(message.getBody()).thenReturn(messageBody);
        when(objectMapper.readValue(messageBody, SkillAcquiredEvent.class))
                .thenThrow(new IOException("Error parsing"));

        Executable executable = () -> listener.onMessage(message, new byte[0]);

        EventProcessingException exception = assertThrows(EventProcessingException.class, executable);
        assertEquals("Failed to process event of type SkillAcquiredEvent", exception.getMessage());
    }
}
