package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.events.RecommendationReceivedEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.time.LocalDateTime;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class RecommendationReceivedMessageBuilderTest {

    @InjectMocks
    private RecommendationReceivedMessageBuilder recommendationReceivedMessageBuilder;

    @Mock
    private MessageSource messageSource;

    @Test
    public void testBuildMessage(){
        RecommendationReceivedEvent event = new RecommendationReceivedEvent(100L, 1L, "Alex", 2L, LocalDateTime.now());
        Mockito.when(messageSource.getMessage(any(), any(), any()))
                .thenReturn("User Alex given recommendation for You");

        String result = recommendationReceivedMessageBuilder.buildMessage(event, Locale.UK);
        Assertions.assertEquals(result, "User Alex given recommendation for You");
    }
}
