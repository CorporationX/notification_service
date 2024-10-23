package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.feign.UserServiceClient;
import faang.school.notificationservice.model.dto.SkillCandidateDto;
import faang.school.notificationservice.exception.EventProcessingException;
import faang.school.notificationservice.model.event.SkillOfferedEvent;
import faang.school.notificationservice.service.MessageBuilder;
import faang.school.notificationservice.service.impl.SkillOfferedMessageBuilder;
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
public class SkillOfferedEventListenerTest {

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

    private SkillOfferedEventListener listener;

    @BeforeEach
    public void setUp() {
        List<NotificationService> notificationServices = Collections.singletonList(notificationService);
        List<MessageBuilder<?>> messageBuilders = Collections.singletonList(
                new SkillOfferedMessageBuilder(userServiceClient, messageSource));

        listener = new SkillOfferedEventListener(objectMapper, userServiceClient,
                notificationServices, messageBuilders);
    }

    @Test
    @DisplayName("Should successfully process SkillOfferedEvent and send notification")
    public void testOnMessage_Success() throws Exception {
        SkillOfferedEvent event = new SkillOfferedEvent();
        event.setReceiverId(1L);
        event.setSenderId(2L);
        event.setSkillId(3L);

        byte[] messageBody = objectMapper.writeValueAsBytes(event);
        when(message.getBody()).thenReturn(messageBody);
        when(objectMapper.readValue(messageBody, SkillOfferedEvent.class)).thenReturn(event);

        UserDto receiverDto = new UserDto();
        receiverDto.setId(1L);
        receiverDto.setUsername("Receiver");

        UserDto senderDto = new UserDto();
        senderDto.setId(2L);
        senderDto.setUsername("Sender");

        SkillDto skillDto = new SkillDto();
        skillDto.setId(3L);
        skillDto.setTitle("FAANG");

        SkillCandidateDto candidate = new SkillCandidateDto();
        candidate.setSkill(skillDto);
        candidate.setOffersAmount(1);

        List<SkillCandidateDto> candidates = Collections.singletonList(candidate);

        when(userServiceClient.getUser(1L)).thenReturn(receiverDto);
        when(userServiceClient.getUser(2L)).thenReturn(senderDto);
        when(userServiceClient.getOfferedSkills(1L)).thenReturn(candidates);
        when(messageSource.getMessage(eq("skill.offered"), any(), eq(Locale.UK))).thenReturn("Notification message");

        listener.onMessage(message, null);

        verify(notificationService, times(1)).send(eq(receiverDto), eq("Notification message"));
    }

    @Test
    @DisplayName("Should throw EventProcessingException when message parsing fails")
    public void testOnMessage_EventProcessingException() throws Exception {
        byte[] messageBody = new byte[0];
        when(message.getBody()).thenReturn(messageBody);
        when(objectMapper.readValue(messageBody, SkillOfferedEvent.class))
                .thenThrow(new IOException("Error parsing"));

        Executable executable = () -> listener.onMessage(message, new byte[0]);

        EventProcessingException exception = assertThrows(EventProcessingException.class, executable);
        assertEquals("Failed to process event of type SkillOfferedEvent", exception.getMessage());
    }
}
