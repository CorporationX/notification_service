package faang.school.notificationservice.message_builder;

import faang.school.notificationservice.dto.RecommendationReceivedEventDto;
import faang.school.notificationservice.messaging.message_builder.RecommendationReceivedEventMessageBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

@ExtendWith(MockitoExtension.class)
public class RecommendationReceivedEventMessageBuilderTest {
    @Mock
    private MessageSource messageSource;

    @InjectMocks
    RecommendationReceivedEventMessageBuilder recommendationReceivedEventMessageBuilder;

    @Test
    void testBuildMessage() {
        String text = "Message Text";

        Mockito.when(messageSource
                .getMessage(Mockito.anyString(), Mockito.any(), Mockito.any(Locale.class))).thenReturn(text);

        String message = recommendationReceivedEventMessageBuilder
                .buildMessage(RecommendationReceivedEventDto.builder().build(), Locale.getDefault());
        Assertions.assertEquals(text, message);
    }
}
