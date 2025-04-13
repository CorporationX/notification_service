package faang.school.notificationservice.service.kafka;

import faang.school.notificationservice.dto.NotificationDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.telegram.TelegramService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class KafkaEventListenerTest {
    @InjectMocks
    private KafkaEventListener kafkaEventListener;

    @Mock
    private TelegramService telegramService;

    private ObjectMapper objectMapper;
    private UserDto user;
    private NotificationDto notificationDto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        user = new UserDto();
        user.setId(123L);
        user.setUsername("john");
        user.setEmail("john@example.com");
        user.setPhone("123456789");
        user.setPreference(UserDto.PreferredContact.TELEGRAM);

        notificationDto = new NotificationDto();
        notificationDto.setUser(user);
        notificationDto.setMessage("Hello from test!");
    }

    @Test
    void testListen() throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(notificationDto);
        ConsumerRecord<String, String> record = new ConsumerRecord<>("notification-topic", 0, 0, null, json);

        kafkaEventListener.listen(record);

        verify(telegramService, times(1)).sendMessage(eq("123"), eq("Hello from test!"));
    }

    @Test
    void testListen_InvalidJson_ShouldThrowRuntimeException() {
        String invalidJson = "{ invalid json }";
        ConsumerRecord<String, String> record = new ConsumerRecord<>("notification-topic", 0, 0, null, invalidJson);

        try {
            kafkaEventListener.listen(record);
        } catch (RuntimeException e) {
            verifyNoInteractions(telegramService);
        }
    }

    @Test
    void testListen_UnknownPreference_ShouldNotCallTelegramService() throws Exception {
        user.setPreference(UserDto.PreferredContact.EMAIL);

        notificationDto.setMessage("You should not receive this in Telegram");

        String json = objectMapper.writeValueAsString(notificationDto);
        ConsumerRecord<String, String> record = new ConsumerRecord<>("notification-topic", 0, 0, null, json);

        kafkaEventListener.listen(record);

        verifyNoInteractions(telegramService);
    }
}