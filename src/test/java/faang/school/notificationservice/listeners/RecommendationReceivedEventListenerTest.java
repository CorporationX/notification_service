package faang.school.notificationservice.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.events.RecommendationReceivedEvent;
import faang.school.notificationservice.messaging.RecommendationReceivedMessageBuilder;
import faang.school.notificationservice.service.telegram.TelegramService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.connection.DefaultMessage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecommendationReceivedEventListenerTest {

    @InjectMocks
    private RecommendationReceivedEventListener recommendationReceivedEventListener;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private MessageSource messageSource;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private TelegramService telegramService;


    @Test
    public void testOnMessage() throws IOException {
        RecommendationReceivedEvent event =
                new RecommendationReceivedEvent(100L, 1L, "Alex", 2L, LocalDateTime.now());
        byte[] bytes = new byte[]{};
        String textNotification = "text";
        UserDto userDto = new UserDto();
        userDto.setId(2L);
        userDto.setPreference(UserDto.PreferredContact.TELEGRAM);
        recommendationReceivedEventListener = new RecommendationReceivedEventListener(
                objectMapper,
                List.of(new RecommendationReceivedMessageBuilder(messageSource)),
                userServiceClient,
                List.of(telegramService));
        when(objectMapper.readValue(bytes, RecommendationReceivedEvent.class))
                .thenReturn(event);
        when(messageSource.getMessage(any(), any(), any())).thenReturn(textNotification);
        when(userServiceClient.getUser(event.getReceiverId())).thenReturn(userDto);
        when(telegramService.getPreferredContact()).thenReturn(UserDto.PreferredContact.TELEGRAM);

        recommendationReceivedEventListener.onMessage(new DefaultMessage(bytes, bytes), null);
        verify(telegramService).send(userDto, textNotification);
    }
}
