package faang.school.notificationservice.subscriber;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.EventRegistrationNotificationDto;
import faang.school.notificationservice.notification.telegram.TelegramMessageSender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventRegistrationListenerTest {

    @InjectMocks
    private EventRegistrationListener eventRegistrationListener;

    @Mock
    private TelegramMessageSender messageSender;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void onMessage_shouldProcessMessagesFromRedisChannel() throws JsonProcessingException, TelegramApiException {
        EventRegistrationNotificationDto notificationDto = new EventRegistrationNotificationDto();
        notificationDto.setTelegramId("123456789");
        notificationDto.setMessage("Test message");
        notificationDto.setEventId(1L);
        notificationDto.setUserId(1L);

        String jsonMessage = objectMapper.writeValueAsString(notificationDto);
        Message message = mock(Message.class);
        when(message.getBody()).thenReturn(jsonMessage.getBytes());

        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue(anyString(), eq(EventRegistrationNotificationDto.class)))
                .thenReturn(notificationDto);

        EventRegistrationListener eventRegistrationListener = new EventRegistrationListener(messageSender);
        eventRegistrationListener.setObjectMapper(objectMapper);

        eventRegistrationListener.onMessage(message, null);

        verify(messageSender, times(1)).sendMessage(notificationDto.getTelegramId(), notificationDto.getMessage());
    }

    @Test
    void onMessage_shouldThrowException_whenJsonIsInvalid() {
        String invalidJsonMessage = "{ invalid json }";
        Message message = mock(Message.class);
        when(message.getBody()).thenReturn(invalidJsonMessage.getBytes());

        assertThrows(IllegalStateException.class, () -> eventRegistrationListener.onMessage(message, null));
    }
}