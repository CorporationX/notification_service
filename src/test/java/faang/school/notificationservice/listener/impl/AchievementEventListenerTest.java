package faang.school.notificationservice.listener.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.feign.UserServiceClient;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.model.enums.PreferredContact;
import faang.school.notificationservice.model.event.AchievementEvent;
import faang.school.notificationservice.service.MessageBuilder;
import faang.school.notificationservice.service.impl.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.util.List;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AchievementEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private Message message;

    @Mock
    private MessageBuilder<AchievementEvent> messageBuilder;

    @Mock
    private EmailService emailService;

    private AchievementEventListener achievementEventListener;

    @BeforeEach
    void setUp() {
        achievementEventListener = new AchievementEventListener(
                objectMapper, userServiceClient, List.of(emailService), List.of(messageBuilder));
    }

    @Test
    void testOnMessage() throws Exception {
        AchievementEvent event = new AchievementEvent(1L, 1L);
        UserDto userDto = new UserDto();
        userDto.setPreference(PreferredContact.EMAIL);

        when(objectMapper.readValue(any(byte[].class), eq(AchievementEvent.class))).thenReturn(event);
        when(messageBuilder.buildMessage(event, Locale.ENGLISH)).thenReturn("Achievement received!");
        when(messageBuilder.getSupportedClass()).thenReturn(AchievementEvent.class);
        when(userServiceClient.getUser(anyLong())).thenReturn(userDto);
        when(emailService.getPreferredContact()).thenReturn(PreferredContact.EMAIL);
        when(message.getBody()).thenReturn(new byte[0]);

        achievementEventListener.onMessage(message, null);

        verify(emailService).send(any(), eq("Achievement received!"));
        verify(userServiceClient).getUser(1L);
    }
}