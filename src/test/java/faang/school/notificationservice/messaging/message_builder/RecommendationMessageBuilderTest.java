package faang.school.notificationservice.builder;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.ProfileViewEvent;
import faang.school.notificationservice.dto.RecommendationReceivedEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.message_builder.ProfileViewMessageBuilder;
import faang.school.notificationservice.messaging.message_builder.RecommendationReceiveMessageBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.time.LocalDateTime;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@ExtendWith(value = {MockitoExtension.class})
public class RecommendationMessageBuilderTest {
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private MessageSource messageSource;
    @InjectMocks
    private RecommendationReceiveMessageBuilder recommendationReceiveMessageBuilder;

    @Test
    public void testBuildMessage() {
        RecommendationReceivedEvent event = RecommendationReceivedEvent.builder()
                .authorId(1L)
                .recipientId(2L)
                .recommendationId(3L)
                .build();
        Locale locale = Locale.ENGLISH;

        String msg = "Congrats! You've got a new recommendation from Diega";
        when(messageSource.getMessage(eq("recommendation.new"), any(), eq(locale)))
                .thenReturn(msg);

        UserDto authorName = UserDto.builder()
                .id(1L)
                .username("Diega")
                .build();

        when(userServiceClient.getUser(1L)).thenReturn(authorName);

        String result = recommendationReceiveMessageBuilder.buildMessage(event, locale);
        assertEquals(msg, result);
    }
}
