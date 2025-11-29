package faang.school.notificationservice.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.Recommendation;
import faang.school.notificationservice.dto.RecommendationEventBuilder;
import faang.school.notificationservice.dto.RecommendationReceivedEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.util.List;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecommendationReceivedEventListenerTest {

    @Mock
    ObjectMapper objectMapper;

    @Mock
    UserServiceClient userServiceClient;

    @Mock
    NotificationService emailService;

    @Mock
    MessageBuilder msgBuilder;

    @Test
    void onMessage_buildsTextAndSendsEmailToReceiver() throws Exception {
        RecommendationReceivedEvent event =
                new RecommendationReceivedEvent(10L, 2L, 1L);

        byte[] body = "dummy".getBytes();
        Message redisMessage = mock(Message.class);
        when(redisMessage.getBody()).thenReturn(body);

        when(objectMapper.readValue(body, RecommendationReceivedEvent.class))
                .thenReturn(event);

        UserDto receiver = new UserDto();
        receiver.setId(1L);
        receiver.setEmail("receiver@test.com");
        receiver.setPreference(UserDto.PreferredContact.EMAIL);

        UserDto author = new UserDto();
        author.setId(2L);
        author.setUsername("Author");

        when(userServiceClient.getUser(1L)).thenReturn(receiver);
        when(userServiceClient.getUser(2L)).thenReturn(author);

        Recommendation rec = new Recommendation(2L, 1L, "Nice!");
        when(userServiceClient.getRecommendation(10L)).thenReturn(rec);

        when(msgBuilder.getInstance()).thenReturn(RecommendationReceivedEvent.class);
        when(msgBuilder.buildMessage(any(RecommendationEventBuilder.class), eq(Locale.UK)))
                .thenReturn("FINAL_TEXT");

        when(emailService.getPreferredContact())
                .thenReturn(UserDto.PreferredContact.EMAIL);

        RecommendationReceivedEventListener listener =
                new RecommendationReceivedEventListener(
                        objectMapper,
                        List.of(emailService),
                        userServiceClient,
                        List.of(msgBuilder)
                );

        listener.onMessage(redisMessage, new byte[0]);

        verify(emailService).send(receiver, "FINAL_TEXT");
        verifyNoMoreInteractions(emailService);
    }
}